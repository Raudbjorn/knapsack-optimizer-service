package service.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Streams;
import org.junit.Test;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.test.TestServer;
import play.test.WithServer;

import java.io.IOException;
import java.util.OptionalInt;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.ACCEPTED;
import static play.mvc.Http.Status.OK;

public class TaskControllerTest extends WithServer {

    private static final String TEST_PROBLEM =
            "{\"problem\": {\"capacity\": 60, \"weights\": [10, 20, 33], \"values\": [10, 3, 30]}}";

    @Test
    public void testCreateTask(){
        int testPort = getTestPort(testServer);
        withTestClient(testPort, client -> {
            WSResponse response = client.url(getUrl(testServer) + "/knapsack/tasks")
                    .setContentType("application/json")
                    .post(TEST_PROBLEM)
                    .toCompletableFuture()
                    .get();
            assertEquals(ACCEPTED, response.getStatus());
        });
    }

    @Test
    public void testGetTask(){
        String taskEndpoint = getUrl(testServer) + "/knapsack/tasks";
        int testPort = getTestPort(testServer);

        withTestClient(testPort, client -> {
            WSResponse postResponse = client.url(taskEndpoint)
                    .setContentType("application/json")
                    .post(TEST_PROBLEM)
                    .toCompletableFuture()
                    .get();
            assertEquals(ACCEPTED, postResponse.getStatus());

            String responseId = Json.parse(postResponse.getBody()).get("task").textValue();
            WSResponse getResponse = client
                    .url(taskEndpoint + "/" + responseId).get()
                    .toCompletableFuture()
                    .get();

            assertEquals(OK, getResponse.getStatus());

            JsonNode getJson = Json.parse(getResponse.getBody());
            String requestId = getJson.get("task").textValue();
            assertEquals(responseId, requestId);

            long numberOfTimeStamps = Streams.stream(getJson.get("timestamps").elements()).count();
            assertEquals(3, numberOfTimeStamps);
        });
    }

    private String getUrl(TestServer server){
        return "http://localhost:" + getTestPort(server);
    }

    private int getTestPort(TestServer server){
        OptionalInt optHttpsPort = server.getRunningHttpsPort();
        return optHttpsPort.orElse(server.getRunningHttpPort()
                .orElseThrow(() -> new RuntimeException("Could not get test server port")));
    }


    private void withTestClient(int port, WSClientConsumer testCode){
        try (WSClient ws = play.test.WSTestClient.newClient(port)) {
            testCode.withClient(ws);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    private interface WSClientConsumer {
        void withClient(WSClient client) throws IOException,InterruptedException,ExecutionException;
    }

}
