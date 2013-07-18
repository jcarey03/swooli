<!doctype html>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
    <head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb#">

        <meta charset="utf-8"/>
        <meta name="title" content="Swooli" />
        <meta name="description" content="Collect and share videos with your friends, family, and the world." />
        <meta name="keywords" content="video, sharing, camera phone, video phone, free, upload, collection, social" />
        <meta property="fb:app_id" content="189917671146406">
        <meta name="og:title" content="Swooli" />
        <meta name="og:description" content="Collect and share videos with your friends, family, and the world." />
        <meta name="og:type" content="website" />
        <meta name="og:url" content="www.swooli.com" />
        <meta name="og:image" content="static/images/logo.png" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> <!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame -->

        <!--[if IE]><![endif]-->
        <!--[if lt IE 7 ]> <body class="ie6"> <![endif]-->
        <!--[if IE 7 ]>    <body class="ie7"> <![endif]-->
        <!--[if IE 8 ]>    <body class="ie8"> <![endif]-->
        <!--[if IE 9 ]>    <body class="ie9"> <![endif<]-->
        <!--[if (gt IE 9)|!(IE)]><!-->  <!--<![endif]-->

        <title>Swooli</title>

        <link rel="shortcut icon" href="static/images/favicon.ico" type="image/x-icon" />
        <link rel="icon" href="static/images/favicon.ico" type="image/x-icon" />

        <link href="static/css/reset.css" rel="stylesheet" />
        <link href="static/css/common.css" rel="stylesheet" />
        <link href="static/css/header.css" rel="stylesheet" />
        <link href="static/css/footer.css" rel="stylesheet" />
        <link href="static/css/videoCollectionRequest.css" rel="stylesheet" />
        <link href="static/css/medes.css" rel="stylesheet" />

        <!-- Grab Google CDN's jQuery. fall back to local if necessary -->
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
        <script>!window.jQuery && document.write('<script src="static/scripts/jquery-1.8.3.min.js"><\/script>');</script>
        <script src="static/scripts/jquery-1.8.3.js"></script>
        <script src="static/scripts/jquery.autosize-1.16.1.js"></script>
        <script src="static/scripts/ellipses.js"></script>
        <script src="static/scripts/common.js"></script>
        <script src="static/scripts/videoCollectionRequest.js"></script>
        <script src="static/scripts/paste.js"></script>
        <script src="static/scripts/jquery.medes-1.0.0.js"></script>

    </head>
    <body>
        <div class="wrapper">
            <div id="header">
                <div id="logo">
                    <a href="/web"></a>
                </div>
                <div id="search">
                    <input type="text" name="search" />
                    <a href="#"></a>
                </div>
                <div id="userNav">
                    <ul class="navList">
                        <li class="nav">
                            <a href="#">About</a>
                            <span class="whiteDownArrow"></span>
                        </li>
                        <li class="nav">
                            <c:choose>
                                <c:when test="${user == null}">
                                    <div id="login" class="button">
                                        <a href="login">Login</a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <img src="${user.photoUri}" alt="User Photo" height="25" width="25" />
                                    ${user.username}
                                </c:otherwise>
                            </c:choose>
                        </li>
                    </ul>
                </div>
            </div>
            <div id="main">
                <div id="createCollectionContainer">
                    <form:form id="videoCollectionForm" action='collections' method='POST' modelAttribute="videoCollectionRequestBean">
                        <p class="formTitle"">Let's create your video collection...</p>
                        <div id="titleContainer">
                            <div class="fieldHeader">Title</div>
                            <form:input path="title" cssClass="innerShadow" />
                            <form:errors path="title" cssClass="error" />
                        </div>
                        <div id="descriptionContainer">
                            <div class="fieldHeader">Description</div>
                            <div id="descriptionWidgetContainer"></div>
                        </div>
                        <div id="sharingContainer">
                            <div class="fieldHeader">Sharing</div>
                            <form:checkbox id="visible" path="visible" />
                            <label for="visible">Only friends can view my video collection</label>
                            <br/>
                            <form:checkbox id="open" path="open" />
                            <label for="open">Only friends can add videos to my video collection</label>
                            <div class="button">
                                <span>Add Friends</span>
                            </div>
                        </div>
                        <div id="videosContainer">
                            <div class="fieldHeader">Add Videos</div>
                            <form:input path="videosUrl" cssClass="innerShadow" value="Enter webpage URL..." />
                            <a href="#"></a>
                        </div>
                    </form:form>
                </div>
            </div>
            <div class="push"></div>
        </div>
        <div id="footer">
            <div id="legalNav">
                <ul class="navList">
                    <li class="nav">
                        <a href="#">&copy; 2012 Swooli</a>
                    </li>
                    <li class="nav">
                        <a href="#">Terms of Service</a>
                    </li>
                    <li class="nav">
                        <a href="#">Privacy Policy</a>
                    </li>
                    <li class="nav">
                        <a href="#">Help</a>
                    </li>
                </ul>
            </div>
        </div>
    </body>
</html>
