package teacheasy.xml.contenthandlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import teacheasy.data.Lesson;
import teacheasy.data.Page;
import teacheasy.data.VideoObject;
import teacheasy.xml.XMLElement;
import teacheasy.xml.util.XMLNotification;
import teacheasy.xml.util.XMLUtil;
import teacheasy.xml.util.XMLNotification.*;

public class VideoXMLHandler extends DefaultHandler{
    private Lesson lesson;
    private Page page;
    private VideoObject video;
    
    private ArrayList<XMLNotification> errorList;
    private XMLReader xmlReader;
    private DefaultHandler parent;
    
    public VideoXMLHandler(XMLReader nXMLReader, DefaultHandler nParent, Lesson nLesson, Page nPage, ArrayList<XMLNotification> nErrorList, Attributes videoAttrs) {
        this.xmlReader = nXMLReader;
        this.parent = nParent;
        
        this.lesson = nLesson;
        this.page = nPage;
        this.errorList = nErrorList;
        
        videoStart(videoAttrs);
    }
    
    public void endElement(String uri, String localName, String qName) {
        switch(XMLElement.check(qName.toUpperCase())) {
            /* If the image element has finished, return to the parent */
            case VIDEO:
                endHandler();
                break;            
            default:
                break;
        }
    }
    
    public void endHandler() {
        page.pageObjects.add(video);
        
        xmlReader.setContentHandler(parent);
    }
    
    public void videoStart(Attributes attrs) {
        String xStartStr = attrs.getValue("xstart");
        String yStartStr = attrs.getValue("ystart");
        String xEndStr = attrs.getValue("xend");
        String sourceFileStr = attrs.getValue("sourcefile");
        
        float xStart = XMLUtil.checkFloat(xStartStr, 0.0f, Level.ERROR, errorList,
                "Page " + lesson.pages.size() + ", Object " + page.getObjectCount() +" (Video) X Start ");
        
        float yStart = XMLUtil.checkFloat(yStartStr, 0.0f, Level.ERROR, errorList,
                "Page " + lesson.pages.size() + ", Object " + page.getObjectCount() +" (Video) Y Start ");
        
        float xEnd = XMLUtil.checkFloat(xEndStr, -1.0f, Level.WARNING, errorList,
                "Page " + lesson.pages.size() + ", Object " + page.getObjectCount() +" (Video) X End ");
        
        if(sourceFileStr == null) {
            errorList.add(new XMLNotification(Level.ERROR,
                "Page " + lesson.pages.size() + ", Object " + page.getObjectCount() +" (Video) Sourcefile "));
            sourceFileStr = new String("null");
        }
        
        video = new VideoObject(xStart, yStart, xEnd, sourceFileStr);
    }
}
