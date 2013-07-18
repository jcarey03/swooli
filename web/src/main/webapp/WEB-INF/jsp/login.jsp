<!doctype html>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html lang="en">
    <head>

        <meta charset="utf-8"/>
        <!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame -->
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

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
        <link href="static/css/login.css" rel="stylesheet" />

        <!-- Grab Google CDN's jQuery. fall back to local if necessary -->
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
	<script>!window.jQuery && document.write('<script src="static/scripts/jquery-1.8.3.min.js"><\/script>');</script>
        <script src="static/scripts/common.js"></script>
        <script src="static/scripts/login.js"></script>

    </head>
    <body>
        <div class="wrapper">
            <div id="header">
                 <div id="logo">
                    <a href="/web"></a>
                </div>
                <div id="createAccount" class="button">
                    <a href="users">Create Account</a>
                </div>
            </div>
            <div id="main">
                <div id="loginContainer" class="outerShadow">

                    <p class="formTitle">Sign-in</p>

                    <div id="fbLoginContainer">

                        <div id="fbLoginError" class="error">${fbLoginError}</div>

                        <form:form id="fbLoginForm" method='POST'>
                            <div class="fieldHeader">Using Facebook Account</div>
                            <input type="hidden" name="loginType" value="facebook" />
                            <div id="fbButton" class="button"></div>
                        </form:form>

                    </div>

                    <div id="loginDividerContainer">
                        <hr />
                        <span>or</span>
                        <hr />
                    </div>

                    <div id="basicLoginContainer">

                        <div id="basicLoginError" class="error">${basicLoginError}</div>

                        <form:form id="loginForm" method='POST' modelAttribute="basicLoginBean">
                            <input type="hidden" name="loginType" value="basic" />

                            <label for="email" class="fieldHeader">Email</label>
                            <form:input path="email" cssClass="innerShadow" />

                            <label for="password" class="fieldHeader">Password</label>
                            <form:password path="password" cssClass="innerShadow" />

                            <div id="rememberMeContainer">
                                <form:checkbox id="rememberMe" path="rememberMe" />
                                <label for="rememberMe">remember me</label>
                            </div>

                            <div id="loginButton" class="button">
                                <span>Sign-in</span>
                            </div>

                            <a id="forgotPassword" href="#">Forgot password?</a>
                        </form:form>

                    </div>

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