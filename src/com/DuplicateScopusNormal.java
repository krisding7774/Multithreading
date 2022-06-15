package com;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DuplicateScopusNormal {

	private static final String root = "D:\\workspace\\change\\example\\storehouse";

	public static void main(String[] args) {
//		產生的SQL語法
		String sql = "UPDATE example SET url = [isbn] WHERE mId = [mId];";

		try {
			List<String> list = new ArrayList<>();
//			讀取路徑位置以及位置內的網址檔案
			List<String> urlList = Files.readAllLines(Paths.get(root, "url.txt"));
//			讀取路徑位置以及位置內的網址檔案
			List<String> mIdList = Files.readAllLines(Paths.get(root, "mId.txt"));
//			將不需要的訊息替換成空白，只留下需要的isbns號
			List<String> isbnList = urlList.stream().map(v -> {
				return v.replace("http://static.example.tw/image/book/", "").replace(" ", "").replace("-", "")
						.replace("b平裝 ", "").replace("$平裝", "").replace(":", "").replace("/large", "");
			}).collect(Collectors.toList());

			for (int i = 0; i < isbnList.size(); i++) {
				list.add(sql.replace("[isbn]", isbnList.get(i)).replace("[mid]", mIdList.get(i)));
			}

//			寫入路徑以及檔案名稱
			Path path = Paths.get(root, "resoult.sql");
//			編碼格式為UTF-8
			Charset cs = Charset.forName("UTF-8");
//			在指定路徑輸出修改後網址，並設定編碼格式為UTF-8
			Files.write(path, list, cs, WRITE, CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}