package com.js.util;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XmlParserUtil {

    /**
     * XML 문자열에서 airlineImage 태그의 값을 추출하여 리턴합니다.
     */
    public static String getAirlineImage(String xmlString) {
        try {
            // 1. DocumentBuilderFactory 생성 및 설정
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // 2. XML 문자열을 입력 스트림으로 변환하여 파싱
            ByteArrayInputStream input = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
            Document doc = builder.parse(input);
            
            // 3. "airlineImage" 태그를 가진 모든 노드를 찾음
            NodeList nodeList = doc.getElementsByTagName("airlineImage");
            
            // 4. 첫 번째 아이템의 텍스트 콘텐츠를 반환
            if (nodeList != null && nodeList.getLength() > 0) {
                return nodeList.item(0).getTextContent();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null; // 값이 없거나 에러 발생 시
    }
}