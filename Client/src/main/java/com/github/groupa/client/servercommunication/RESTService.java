package com.github.groupa.client.servercommunication;

import com.github.groupa.client.jsonobjects.ImageInfo;
import com.github.groupa.client.jsonobjects.ImageList;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

public interface RESTService {
	@GET("/images")
	public ImageList getImageList();

	@GET("/images/{id}")
	public ImageInfo getImageInfo(@Path("id") long id);

	@GET("/images/{id}/raw")
	public Response getImageRaw(@Path("id") long id);

	@GET("/images/{id}/compressed")
	public Response getImageCompressed(@Path("id") long id);

	@GET("/images/{id}/xs")
	public Response getThumbnailXSmall(@Path("id") long id);

	@GET("/images/{id}/s")
	public Response getThumbnailSmall(@Path("id") long id);

	@GET("/images/{id}/m")
	public Response getThumbnailMedium(@Path("id") long id);

	@GET("/images/{id}/l")
	public Response getThumbnailLarge(@Path("id") long id);

	@GET("/images/{id}/xl")
	public Response getThumbnailXLarge(@Path("id") long id);
	
	@GET("/tags/{tag}")
	public ImageList getImageListForTag(@Path("tag") String tag);
	
	@Multipart
	@POST("/image")
	public ImageInfo uploadImage(@Part("value") TypedFile image);
	
	@FormUrlEncoded
	@POST("/images/{id}/rate")
	public Response rateImage(@Path("id") long id, @Field("value") int rating);
	
	@FormUrlEncoded
	@POST("/images/{id}/describe")
	public Response describeImage(@Path("id") long id, @Field("value") String description);
	
	@FormUrlEncoded
	@POST("/images/{id}/tag")
	public Response tagImage(@Path("id") long id, @Field("value") String tags);
}
