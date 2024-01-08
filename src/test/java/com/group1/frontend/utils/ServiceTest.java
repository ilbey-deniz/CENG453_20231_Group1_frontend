package com.group1.frontend.utils;

import com.group1.frontend.dto.httpDto.GameRoom_PlayerDto;
import com.group1.frontend.dto.httpDto.LoginDto;
import com.group1.frontend.dto.httpDto.PlayerDto;
import com.group1.frontend.dto.websocketDto.PlayerReadyDto;
import com.group1.frontend.enums.PlayerColor;
import org.junit.Before;
import org.junit.Test;

import java.net.http.HttpResponse;

import static com.group1.frontend.constants.LobbyPlayerConstants.getRandomAvailableColor;
import static org.junit.Assert.*;

public class ServiceTest {
    Service service;
    Service service2;

    @Before
    public void setUp() throws Exception {
        service = new Service();
        service.setBackendURL("http://localhost:8080/api");
        String username = "veli";
        String password = "veli";
        HttpResponse<String> response = service.makeRequest(
                "/login",
                "POST",
                new LoginDto(username, password));
        if(response.statusCode() == 200) {
            service.setToken(response.body());
            service.setUsername(username);
        }

        // create a new service to test the join game
        service2 = new Service();
        service2.setBackendURL("http://localhost:8080/api");
        String username2 = "admin";
        String password2 = "123";
        HttpResponse<String> response2 = service2.makeRequest(
                "/login",
                "POST",
                new LoginDto(username2, password2));
        if(response2.statusCode() == 200) {
            service2.setToken(response2.body());
            service2.setUsername(username2);
        }
    }

    @Test public void testLogin() {
        // test login with uncorrect username and password
        String username = "veli12";
        String password = "veli21";
        HttpResponse<String> response = service.makeRequest(
                "/login",
                "POST",
                new LoginDto(username, password));
        if(response.statusCode() == 200) {
            // this means that the login is successful, which is not expected
            assert false;
        }
        else {
            assert true;
        }
        // test login with correct username and password
        username = "admin";
        password = "123";
        response = service.makeRequest(
                "/login",
                "POST",
                new LoginDto(username, password));
        if(response.statusCode() == 200) {
            // this means that the login is successful, which is expected
            assert true;
        }
        else {
            assert false;
        }
    }

    @Test
    public void testCreateGame(){
        PlayerColor color = getRandomAvailableColor();
        PlayerDto playerDto = new PlayerDto(
                service.getUsername(),
                color,
                false,
                true,
                false);

        HttpResponse<String> response = service.makeRequestWithToken(
                "/game/create",
                "POST",
                playerDto);

        if(response.statusCode() == 200) {
            String roomCode = response.body();
            String regex = "[0-9]{5}";
            if(roomCode.matches(regex)){
                assert true;
            }
            else{
                assert false;
            }
        }
        else {
            assert false;
        }
    }

    @Test
    public void testCreateAndJoinGame(){

        // create a game with existing service
        PlayerColor color = getRandomAvailableColor();
        PlayerDto playerDto = new PlayerDto(
                service.getUsername(),
                color,
                false,
                true,
                false);
        HttpResponse<String> response = service.makeRequestWithToken(
                "/game/create",
                "POST",
                playerDto);

        if(response.statusCode() == 200) {
            service.createGameRoom();
            service.getGameRoom().setRoomCode(response.body());
            LobbyPlayer lobbyPlayer = new LobbyPlayer();
            lobbyPlayer.setName(playerDto.getName());
            lobbyPlayer.setColor(playerDto.getColor());
            lobbyPlayer.setCpu(playerDto.isCpu());
            lobbyPlayer.setReady(playerDto.isReady());
            service.getGameRoom().addPlayer(lobbyPlayer);
            service.getGameRoom().setHostName(service.getUsername());
        }

        // join the game with service2
        PlayerDto playerDto2 = new PlayerDto(
                service2.getUsername(),
                color,
                false,
                false,
                false);
        GameRoom_PlayerDto gameRoomPlayerDto = new GameRoom_PlayerDto(
                service.getGameRoom().getRoomCode(),
                playerDto2
        );
        HttpResponse<String> response3 = service2.makeRequestWithToken(
                "/game/join",
                "POST",
                gameRoomPlayerDto);
        if(response3.statusCode() == 200) {
            assert true;
        }
        else {
            assert false;
        }
    }

    @Test
    public void testReadyAndStartGame(){
        PlayerColor color = getRandomAvailableColor();
        PlayerDto playerDto = new PlayerDto(
                service.getUsername(),
                color,
                false,
                true,
                false);
        HttpResponse<String> response = service.makeRequestWithToken(
                "/game/create",
                "POST",
                playerDto);

        LobbyPlayer lobbyPlayer = new LobbyPlayer();

        if(response.statusCode() == 200) {
            service.createGameRoom();
            service.getGameRoom().setRoomCode(response.body());
            lobbyPlayer.setName(playerDto.getName());
            lobbyPlayer.setColor(playerDto.getColor());
            lobbyPlayer.setCpu(playerDto.isCpu());
            lobbyPlayer.setReady(playerDto.isReady());
            service.getGameRoom().addPlayer(lobbyPlayer);
            service.getGameRoom().setHostName(service.getUsername());
        }
        // join the game with service2
        PlayerDto playerDto2 = new PlayerDto(
                service2.getUsername(),
                color,
                false,
                false,
                false);
        GameRoom_PlayerDto gameRoomPlayerDto = new GameRoom_PlayerDto(
                service.getGameRoom().getRoomCode(),
                playerDto2
        );
        HttpResponse<String> response3 = service2.makeRequestWithToken(
                "/game/join",
                "POST",
                gameRoomPlayerDto);
        int joinCount = 0;
        if(response3.statusCode() == 200) {
            joinCount++;
        }
        // check the game started when players are ready
        playerDto.setReady(true);
        GameRoom_PlayerDto gameRoomPlayerDto2 = new GameRoom_PlayerDto(
                service.getGameRoom().getRoomCode(),
                playerDto
        );
        HttpResponse<String> response4 = service.makeRequestWithToken(
                "/game/playerReady",
                "POST",
                gameRoomPlayerDto2);
        int readyCount = 0;
        if(response4.statusCode() == 200) {
            readyCount++;
        }
        playerDto2.setReady(true);
        GameRoom_PlayerDto gameRoomPlayerDto3 = new GameRoom_PlayerDto(
                service.getGameRoom().getRoomCode(),
                playerDto2
        );
        HttpResponse<String> response5 = service2.makeRequestWithToken(
                "/game/playerReady",
                "POST",
                gameRoomPlayerDto3);
        if(response5.statusCode() == 200) {
            readyCount++;
        }

        // start the game and check the game started
        GameRoom_PlayerDto gameRoomPlayerDto4 = new GameRoom_PlayerDto(
                service.getGameRoom().getRoomCode(),
                playerDto
        );
        HttpResponse<String> response6 = service.makeRequestWithToken(
                "/game/start",
                "POST",
                gameRoomPlayerDto4);
        if(response6.statusCode() == 200) {
            service.getGameRoom().setIsStarted(true);
        }

        assertEquals(2, readyCount);
        assertEquals(1, joinCount);
        assertEquals(true, service.getGameRoom().getIsStarted());
    }
}