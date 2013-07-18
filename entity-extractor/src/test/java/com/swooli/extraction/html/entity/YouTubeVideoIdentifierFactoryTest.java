package com.swooli.extraction.html.entity;

import com.swooli.extraction.html.media.YouTubeVideoMetadataFactory.VideoStrategy;
import static org.junit.Assert.*;
import org.junit.Test;
public class YouTubeVideoIdentifierFactoryTest {

    @Test
    public void testObjEmbedPattern1() {
        String url = "http://www.youtube-nocookie.com/v/okEmwaLUemU?version=3&amp;hl=en_US&amp;rel=0";
        assertEquals("okEmwaLUemU", VideoStrategy.OBJ_EMBED.extractVideoId(url));
    }

    @Test
    public void testObjEmbedPattern2() {
        String url = "http://www.youtube.com/v/okEmwaLUemU";
        assertEquals("okEmwaLUemU", VideoStrategy.OBJ_EMBED.extractVideoId(url));
    }

    @Test
    public void testObjEmbedPattern3() {
        String url = "http://www.youtube-nocookie.com/FOO/okEmwaLUemU/";
        assertNull(VideoStrategy.OBJ_EMBED.extractVideoId(url));
    }

    @Test
    public void testIFramePattern1() {
        String url = "http://www.youtube-nocookie.com/embed/okEmwaLUemU?rel=0";
        assertEquals("okEmwaLUemU", VideoStrategy.IFRAME.extractVideoId(url));
    }

    @Test
    public void testIFramePattern2() {
        String url = "https://www.youtube.com/embed/okEmwaLUemU?rel=0";
        assertEquals("okEmwaLUemU", VideoStrategy.IFRAME.extractVideoId(url));
    }

    @Test
    public void testIFramePattern3() {
        String url = "http://www.youtube-nocookie.com/FOO/okEmwaLUemU?rel=0";
        assertNull(VideoStrategy.IFRAME.extractVideoId(url));
    }

    @Test
    public void testShortLinkPattern1() {
        String url = "http://youtu.be/okEmwaLUemU";
        assertEquals("okEmwaLUemU", VideoStrategy.SHORT_LINK.extractVideoId(url));
    }

    @Test
    public void testShortLinkPattern2() {
        String url = "https://youtu.be/okEmwaLUemU?hello";
        assertEquals("okEmwaLUemU", VideoStrategy.SHORT_LINK.extractVideoId(url));
    }

    @Test
    public void testShortLinkPattern3() {
        String url = "http://youtu.be/FOO/okEmwaLUemU";
        assertNull(VideoStrategy.SHORT_LINK.extractVideoId(url));
    }

    @Test
    public void testDefaultPattern1() {
        String url = "http://www.youtube.com/watch?v=okEmwaLUemU";
        assertEquals("okEmwaLUemU", VideoStrategy.DEFAULT.extractVideoId(url));
    }

    @Test
    public void testDefaultPattern2() {
        String url = "https://www.youtube.com/watch?v=okEmwaLUemU&a";
        assertEquals("okEmwaLUemU", VideoStrategy.DEFAULT.extractVideoId(url));
    }

    @Test
    public void testDefaultPatterne() {
        String url = "http://www.youtube.com/watch?va=okEmwaLUemU";
        assertNull(VideoStrategy.DEFAULT.extractVideoId(url));
    }

}