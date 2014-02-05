package helpers;

import play.libs.Json;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonHelper {
	public static ObjectNode badRequestNode() {
		return basicNode(400, "bad request");
	}

	public static ObjectNode okNode() {
		return basicNode(200, "ok");
	}

	public static ObjectNode notFoundNode() {
		return basicNode(404, "not found");
	}

	public static ObjectNode serverErrorNode() {
		return basicNode(500, "internal server error");
	}

	private static ObjectNode basicNode(int status, String message) {
		ObjectNode node = Json.newObject();
		ObjectNode metadata = Json.newObject();

		node.put("metadata", metadata);

		metadata.put("status", status);
		metadata.put("message", message);

		return node;
	}
}
