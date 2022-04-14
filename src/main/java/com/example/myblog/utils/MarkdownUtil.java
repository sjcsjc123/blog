package com.example.myblog.utils;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.StringTokenizer;


/**
 * @author SJC
 */
public class MarkdownUtil {
	// 使用基础功能
    public String markdownToHtml(String markdown){
        MutableDataSet options = new MutableDataSet();

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
        Node document = parser.parse(markdown);
        String html = renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
        return html;
    }

    public static void main(String[] args) {
        String content = "\"<p>测试<img src=\\\"https://sjc-mall\" +\n" +
                "                \".oss-cn-hangzhou\" +\n" +
                "                \".aliyuncs.com/test" +
                ".png?versionId=CAEQMhiBgMC98\" +\n" +
                "                \"" +
                ".OI9xciIGUzNjM1ZGUwZjc2YzQyMDQ5MTAyMjE5YTFkY2EwY2Jh\\\" \" " +
                "+\n" +
                "                \"alt=\\\"\\\" /></p>\\n\"";
        Document document = Jsoup.parse(content);
        Elements img = document.select("img");
        System.out.println(img);
        for (Element element : img) {
            String url = element.attr("src");
            System.out.println(url);
            StringTokenizer stringTokenizer = new StringTokenizer(content,url);
            System.out.println(stringTokenizer.nextToken());
        }
    }
}