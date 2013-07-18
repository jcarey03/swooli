package com.swooli.extraction.html.entity;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HtmlEntityHandler extends DefaultHandler {

    private boolean inHead;
    private boolean inTitle;
    private StringBuilder titleContent = new StringBuilder();

    private String origin;
    private TitleEntity titleEntity;
    private final List<MetaEntity> metaEntities = new ArrayList<>();
    private final List<IFrameEntity> iFrameEntities = new ArrayList<>();
    private final List<EmbedEntity> embedEntities = new ArrayList<>();
    private final List<ImageEntity> imageEntities = new ArrayList<>();

    public HtmlEntityHandler(final String origin) {
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    public TitleEntity getTitleEntity() {
        return titleEntity;
    }

    public void setTitleEntity(final TitleEntity entity) {
        this.titleEntity = entity;
    }

    public List<MetaEntity> getMetaEntities() {
        return metaEntities;
    }

    public List<IFrameEntity> getIFrameEntities() {
        return iFrameEntities;
    }

    public List<EmbedEntity> getEmbedEntities() {
        return embedEntities;
    }

    public List<ImageEntity> getImageEntities() {
        return imageEntities;
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName,
            final Attributes attributes) throws SAXException {

        final String tagName = localName.toLowerCase();
        if("head".equals(tagName)) {
            inHead = true;
            return;
        }

        if(inHead) {

            switch (tagName) {
                case "meta":

                    String propertyValue = null;
                    String contentValue = null;
                    for(int i = 0; i < attributes.getLength(); i++) {
                        final String name = attributes.getLocalName(i).toLowerCase();
                        switch (name) {
                            case "property":
                            case "name":
                                propertyValue = attributes.getValue(i);
                                break;
                            case "content":
                                contentValue = attributes.getValue(i);
                                break;
                        }
                    }

                    if(propertyValue != null && contentValue != null) {
                        final MetaEntity entity = new MetaEntity(origin);
                        entity.setProperty(propertyValue);
                        entity.setContent(contentValue);
                        metaEntities.add(entity);
                    }

                    break;
                case "title":
                    inTitle = true;
                    break;
            }
        } else {

            switch (tagName) {
                case "iframe":
                    for(int i = 0; i < attributes.getLength(); i++) {
                        final String name = attributes.getLocalName(i).toLowerCase();
                        if("src".equals(name)) {
                            final IFrameEntity entity = new IFrameEntity(origin);
                            entity.setSrc(attributes.getValue(i));
                            iFrameEntities.add(entity);
                            break;
                        }
                    }
                    break;
                case "embed":
                    for(int i = 0; i < attributes.getLength(); i++) {
                        final String name = attributes.getLocalName(i).toLowerCase();
                        if("src".equals(name)) {
                            final EmbedEntity entity = new EmbedEntity(origin);
                            entity.setSrc(attributes.getValue(i));
                            embedEntities.add(entity);
                            break;
                        }
                    }
                    break;
                case "img":

                    final ImageEntity entity = new ImageEntity(origin);

                    for(int i = 0; i < attributes.getLength(); i++) {
                        final String name = attributes.getLocalName(i).toLowerCase();
                        switch (name) {
                            case "src":
                                entity.setSrc(attributes.getValue(i));
                                break;
                            case "height":
                                entity.setHeight(stringToHtmlSize(attributes.getValue(i)));
                                break;
                            case "width":
                                entity.setWidth(stringToHtmlSize(attributes.getValue(i)));
                                break;
                        }
                    }

                    if(entity.getSrc() != null) {
                        imageEntities.add(entity);
                    }

                    break;
            }
        }

    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if(inTitle) {
            titleContent.append(ch, start, length);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {

        final String tagName = localName.toLowerCase();
        switch (tagName) {
            case "head":
                inHead = false;
                break;
            case "title":
                titleEntity = new TitleEntity(origin);
                titleEntity.setTitle(titleContent.toString());
                inTitle = false;
                break;
        }

    }

    private int stringToHtmlSize(final String value) {

        if(value == null || value.isEmpty()) {
            return 0;
        } else {
            final StringBuilder strb = new StringBuilder();
            for(int i = 0; i < value.length(); i++) {
                final char c = value.charAt(i);
                if(Character.isDigit(c)) {
                    strb.append(c);
                }
            }
            if(strb.length() > 0) {
                return Integer.parseInt(strb.toString());
            } else {
                return 0;
            }
        }

    }

}