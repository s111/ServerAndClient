package com.github.groupa.client.servercommunication;

import static org.junit.Assert.assertEquals;

import java.net.ConnectException;

import org.junit.Before;
import org.junit.Test;

import retrofit.RestAdapter;

import com.github.groupa.client.jsonobjects.ImageList;

public class RESTServiceTest {
	private static final String FAKE_ENDPOINT = "http://fakehost:9431/api";

	private RESTService restService;

	@Before
	public void setUp() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(FAKE_ENDPOINT).setClient(new MockClient()).build();

		restService = restAdapter.create(RESTService.class);
	}

	@Test
	public void getImageList_offset_0_limit_2_expect_two_images()
			throws ConnectException {
		ImageList imageList = restService.getImageList(0, 2);

		assertEquals(2, imageList.getImages().size());
	}
}
