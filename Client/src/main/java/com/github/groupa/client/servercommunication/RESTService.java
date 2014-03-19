package com.github.groupa.client.servercommunication;

import java.net.ConnectException;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.HEAD;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.jsonobjects.ImageList;

public interface RESTService {
	@GET("/images")
	public ImageList getImageList(@Query("offset") int offset,
			@Query("limit") int limit) throws ConnectException;

	@GET("/images/{id}")
	public ImageInfo getImageInfo(@Path("id") long id) throws ConnectException;

	@GET("/images/{id}/raw")
	public Response getImageRaw(@Path("id") long id) throws ConnectException;

	@GET("/images/{id}/compressed")
	public Response getImageCompressed(@Path("id") long id)
			throws ConnectException;

	@GET("/images/{id}/xs")
	public Response getThumbnailXSmall(@Path("id") long id)
			throws ConnectException;

	@GET("/images/{id}/s")
	public Response getThumbnailSmall(@Path("id") long id)
			throws ConnectException;

	@GET("/images/{id}/m")
	public Response getThumbnailMedium(@Path("id") long id)
			throws ConnectException;

	@GET("/images/{id}/l")
	public Response getThumbnailLarge(@Path("id") long id)
			throws ConnectException;

	@GET("/images/{id}/xl")
	public Response getThumbnailXLarge(@Path("id") long id)
			throws ConnectException;

	@GET("/tags/{tag}")
	public ImageList getImageListForTag(@Path("tag") String tag)
			throws ConnectException;

	@Multipart
	@POST("/image")
	public ImageInfo uploadImage(@Part("value") TypedFile image)
			throws ConnectException;

	@FormUrlEncoded
	@POST("/images/{id}/rate")
	public Response rateImage(@Path("id") long id, @Field("value") int rating)
			throws ConnectException;

	@FormUrlEncoded
	@POST("/images/{id}/describe")
	public Response describeImage(@Path("id") long id,
			@Field("value") String description) throws ConnectException;

	@FormUrlEncoded
	@POST("/images/{id}/tag")
	public Response tagImage(@Path("id") long id, @Field("value") String tags)
			throws ConnectException;

	@HEAD("/images")
	public Response hasImageListChanged(@Query("offset") int offset,
			@Query("limit") int limit) throws ConnectException;

	@HEAD("/images/{id}")
	public Response hasImageInfoChanged(@Path("id") long id)
			throws ConnectException;

	@HEAD("/images/{id}/raw")
	public Response hasImageRawChanged(@Path("id") long id)
			throws ConnectException;
}
