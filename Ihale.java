package ihale;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Ihale {

    int finalCount = 0;
    Response response = null;
    String domain = "https://ilan.gov.tr/";

    public static void main(String[] args) {
        try {
            Ihale get = new Ihale();
            get.run();
        } catch (IOException ex) {
            Logger.getLogger(Ihale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() throws IOException {

        Document content;
        int i = 0;
        while (true) {
            i++;
            content = gethtml("https://ilan.gov.tr/kategori-arama?currentPage=" + i + "&npdab=on&type=21628");
            if (content != null) {
                allLinks(content);
            } else {
                System.out.println("The link could not crawl.");
                System.out.println("Till now," + finalCount + " URLs couldn't crawl.");
                break;
            }
        }
    }

    public Document gethtml(String url) {
        Document control;
        try {
            control = Jsoup.connect(url).get();
            return control;
        } catch (IOException exception) {
            return null;
        }
    }

    public void allLinks(Element content) {
        Elements links = content.select("div.inner-item > a");
        Elements lastPoints;
        Document insideLink;
        Elements insideItem;
        String href;
        for (int i = 0; i < links.size(); i++) {
            href = domain + links.get(i).attr("href");
            insideLink = gethtml(href);
            if (insideLink != null) {
                insideItem = insideLink.getElementsByClass("table-div").select("div.tr > div.th");
                lastPoints = insideLink.getElementsByClass("table-div").select("div.tr > div.td");
                for (int j = 0; j < insideItem.size(); j++) {
                    if (insideItem.get(j).text().contains("İhale Kayıt No")
                            || insideItem.get(j).text().contains("Niteliği, Türü ve Miktarı")
                            || insideItem.get(j).text().contains("İşin Yapılacağı Yer")
                            || insideItem.get(j).text().contains("İhale Türü")) {
                        System.out.println(insideItem.get(j).text());
                        System.out.println(lastPoints.get(j / 2).text());
                    }
                }
                System.out.println("\u001b[42m"+"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            } else {
                finalCount++;
            }
        }

    }
}
