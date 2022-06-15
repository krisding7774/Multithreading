package com;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class DuplicateScopusThread extends Thread {

	private String root, mId, thread, url;

	public DuplicateScopusThread(String root, String mId, String thread, String url) {
		super();
		this.root = root;
		this.mId = mId;
		this.thread = thread;
		this.url = url;
	}

	public List<String> replaceAPI() throws IOException {
//		需將此API的isbns號更改為資料庫內的isbns號
		String api = "http://127.0.0.1:8080/example/API?isbns=[isbn]&key=g72e6-cone2-h7sc6-j50mr-mpyj3";

		List<String> list = new ArrayList<>();
//		讀取路徑位置以及位置內的網址檔案
		List<String> urlList = Files.readAllLines(Paths.get(this.root, this.url));
//		將不需要的訊息替換成空白，只留下需要的isbns號
		List<String> isbnList = urlList.stream().map(isbn -> {
			return isbn.replace("http://static.example.tw/image/book/", "").replace(" ", "").replace("-", "")
					.replace("b平裝 ", "").replace("$平裝", "").replace(":", "").replace("/large", "");
		}).collect(Collectors.toList());

//		利用迴圈將api的[isbn]換成實際存在的isbn號
		for (int i = 0; i < urlList.size(); i++) {
			list.add(api.replace("[isbn]", isbnList.get(i)));
		}

		return list;
	}

	@Override
	public void run() {
//		產生的SQL語法
		String sql = "UPDATE example SET url = [isbn] WHERE mId = [mId];";

		try {
//			讀取路徑位置以及位置內的網址檔案
			List<String> mIdList = Files.readAllLines(Paths.get(this.root, this.mId));
			List<String> isbnList = new ArrayList<>();
			List<String> list = new ArrayList<>();
//			純粹計算此程式跑了第幾次
			int count = 1;

			for (String api : replaceAPI()) {
//				由於需要查看網站狀態，故使用URLConnection來訪問URL網址
				URLConnection conn = new URL(api).openConnection();
//				透過getHeaderField查看HTTP的連線狀況，如果是404、405、500則忽略不會進行讀寫
				if (conn.getHeaderField(0).equals("HTTP/1.1 200 OK")) {
					BufferedReader bufferedRead = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
//					將讀出來的json格式轉換成obj，方便獲取img資料
					JSONObject obj = JSON.parseObject(bufferedRead.readLine());
					isbnList.add(obj.getString("img"));
					System.out.println("API No." + this.thread + " is running... frequency：" + count++);
				}
			}
//			把計數器重置繼續算跑幾次	
			count = 1;

//			將SQL語法替換成想要的格式
			for (int i = 0; i < isbnList.size(); i++) {
				list.add(sql.replace("[isbn]", isbnList.get(i)).replace("[mId]", mIdList.get(i)));
				System.out.println("SQL No." + this.thread + "is running... frequency：" + count++);
			}
//			寫入路徑以及檔案名稱
			Path path = Paths.get(this.root, this.thread + ".sql");
//			編碼格式為UTF-8
			Charset cs = Charset.forName("UTF-8");
//			在指定路徑輸出修改後網址，並設定編碼格式為UTF-8
			Files.write(path, list, cs, WRITE, CREATE);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}