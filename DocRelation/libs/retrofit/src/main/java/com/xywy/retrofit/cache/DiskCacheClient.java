/*******************************************************************************
 * Copyright 2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.xywy.retrofit.cache;

import android.util.Log;

import com.google.gson.Gson;
import com.xywy.util.L;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import okhttp3.Request;
import okio.Buffer;
import retrofit2.Response;

/**
 * Disk cache based on "Least-Recently Used" principle. Adapter pattern, adapts
 * {@link DiskLruCache DiskLruCache} to
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see FileNameGenerator
 * @since 1.9.2
 */
public class DiskCacheClient implements ICache{
	/** {@value */
	public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb

	private static final String ERROR_ARG_NULL = " argument must be not null";
	private static final String ERROR_ARG_NEGATIVE = " argument must be positive number";

	protected DiskLruCache cache;
	private File reserveCacheDir;

	protected final FileNameGenerator fileNameGenerator;

	protected int bufferSize = DEFAULT_BUFFER_SIZE;


	/**
	 * @param cacheDir          Directory for file caching
	 * @param cacheMaxSize      Max cache size in bytes. <b>0</b> means cache size is unlimited.
	 * @throws IOException if cache can't be initialized (e.g. "No space left on device")
	 */
	public DiskCacheClient(File cacheDir, long cacheMaxSize) throws IOException {
		this(cacheDir, null, new Md5FileNameGenerator(), cacheMaxSize, Integer.MAX_VALUE);
	}

	/**
	 * @param cacheDir          Directory for file caching
	 * @param reserveCacheDir   null-ok; Reserve directory for file caching. It's used when the primary directory isn't available.
	 * @param fileNameGenerator {@linkplain FileNameGenerator
	 *                          Name generator} for cached files. Generated names must match the regex
	 *                          <strong>[a-z0-9_-]{1,64}</strong>
	 * @param cacheMaxSize      Max cache size in bytes. <b>0</b> means cache size is unlimited.
	 * @param cacheMaxFileCount Max file count in cache. <b>0</b> means file count is unlimited.
	 * @throws IOException if cache can't be initialized (e.g. "No space left on device")
	 */
	public DiskCacheClient(File cacheDir, File reserveCacheDir, FileNameGenerator fileNameGenerator, long cacheMaxSize,
						   int cacheMaxFileCount) throws IOException {
		if (cacheDir == null) {
			throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
		}
		if (cacheMaxSize < 0) {
			throw new IllegalArgumentException("cacheMaxSize" + ERROR_ARG_NEGATIVE);
		}
		if (cacheMaxFileCount < 0) {
			throw new IllegalArgumentException("cacheMaxFileCount" + ERROR_ARG_NEGATIVE);
		}
		if (fileNameGenerator == null) {
			throw new IllegalArgumentException("fileNameGenerator" + ERROR_ARG_NULL);
		}

		if (cacheMaxSize == 0) {
			cacheMaxSize = Long.MAX_VALUE;
		}

		this.reserveCacheDir = reserveCacheDir;
		this.fileNameGenerator = fileNameGenerator;
		initCache(cacheDir, reserveCacheDir, cacheMaxSize, cacheMaxFileCount);
	}

	private void initCache(File cacheDir, File reserveCacheDir, long cacheMaxSize, int cacheMaxFileCount)
			throws IOException {
		try {
			cache = DiskLruCache.open(cacheDir, 1, 1, cacheMaxSize, cacheMaxFileCount);
		} catch (IOException e) {
			L.ex(e);
			if (reserveCacheDir != null) {
				initCache(reserveCacheDir, null, cacheMaxSize, cacheMaxFileCount);
			}
			if (cache == null) {
				throw e; //new RuntimeException("Can't initialize disk cache", e);
			}
		}
	}

	public File getDirectory() {
		return cache.getDirectory();
	}

	@Override
	public boolean remove(Request req) {
		try {
			return cache.remove(getKey(req));
		} catch (IOException e) {
			L.ex(e);
			return false;
		}
	}

	public void close() {
		try {
			cache.close();
		} catch (IOException e) {
			L.ex(e);
		}
		cache = null;
	}
	@Override
	public void clear() {
		try {
			cache.delete();
		} catch (IOException e) {
			L.ex(e);
		}
		try {
			initCache(cache.getDirectory(), reserveCacheDir, cache.getMaxSize(), cache.getMaxFileCount());
		} catch (IOException e) {
			L.ex(e);
		}
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	final static String TAG="Cache";
	@Override
	public String get(Request request) {
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = cache.get(getKey(request));
			if (snapshot != null){
				return snapshot.getString(0);
			}
		} catch (IOException e) {
			Log.e(TAG,e.toString());
			return null;
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}
		return null;
	}
	@Override
	public boolean put(Response response) throws IOException {
		DiskLruCache.Editor editor = cache.edit(getKey(response.raw().request()));
		if (editor == null) {
			return false;
		}
		String jsonResp=new Gson().toJson(response.body());
		boolean savedSuccessfully = false;
		OutputStream os = new BufferedOutputStream(editor.newOutputStream(0), bufferSize);
		try {
			os.write(jsonResp.getBytes());
			savedSuccessfully=true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
				try {
					os.close();
				} catch (Exception ignored) {
				}
		}
		if (savedSuccessfully) {
			editor.commit();
		} else {
			editor.abort();
		}
		return savedSuccessfully;
	}
	private String getKey(Request request) {
		String url=request.url().toString();
		String body="";
		try {
			if (request.body() != null) {
				Buffer sink=new Buffer();
				request.body().writeTo(sink);
				body = sink.readString(Charset.forName("UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileNameGenerator.generate(url+body);
	}
}
