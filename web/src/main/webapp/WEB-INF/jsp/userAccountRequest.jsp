<!doctype html>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html lang="en">
        <head>

        <meta charset="utf-8"/>
        <!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame -->
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

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
        <link href="static/css/userAccountRequest.css" rel="stylesheet" />

        <!-- Grab Google CDN's jQuery. fall back to local if necessary -->
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
        <script type="text/javascript" src="http://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
	<script>!window.jQuery && document.write('<script src="static/scripts/jquery-1.8.3.min.js"><\/script>');</script>
        <script src="static/scripts/common.js"></script>
        <script src="static/scripts/userAccountRequest.js"></script>

    </head>
    <body>
        <div class="wrapper">
            <div id="header">
                 <div id="logo">
                    <a href="/web"></a>
                </div>
            </div>
            <div id="main">
                <div id="accountFormContainer" class="outerShadow">
                    <form:form id="accountForm" action='users' method='POST' modelAttribute="userAccountRequestBean">
                        <p>Create Account</p>
                        <div id="nameContainer">
                            <div class="fieldHeader">Name</div>
                            <form:input path="firstName" cssClass="innerShadow" />
                            <form:input path="lastName" cssClass="innerShadow" />
                            <form:errors path="firstName" cssClass="error" />
                            <form:errors path="lastName" cssClass="error" />
                        </div>
                        <div id="emailContainer">
                            <div class="fieldHeader">Email</div>
                            <form:input path="email" cssClass="innerShadow" />
                            <form:errors path="email" cssClass="error" />
                        </div>
                        <div id="passwordContainer">
                            <div class="fieldHeader">
                                <span>Choose a Password</span>
                                <span id="passwordStrength"></span>
                            </div>
                            <form:password path="password" cssClass="innerShadow" />
                            <form:errors path="password" cssClass="error" />
                        </div>
                        <div id="confirmPasswordContainer">
                            <div class="fieldHeader">Confirm your Password</div>
                            <form:password path="confirmPassword" class="innerShadow" />
                            <form:errors path="confirmPassword" cssClass="error" />
                        </div>
                        <div id="genderContainer">
                            <div class="fieldHeader">Gender</div>
                            <form:select path="gender" items="${genders}" cssClass="innerShadow" />
                            <form:errors path="gender" cssClass="error" />
                        </div>
                        <div id="recaptchaWidgetContainer">
                            <div class="fieldHeader">Are you human?</div>
                            <div id="recaptchaWidget"></div>
                            <form:errors path="captchaError" cssClass="error" />
                        </div>
                        <div id="tosContainer">
                            <span>
                                By creating this account, I agree to the
                                <a href="#">Terms of Service</a> and <a href="#">Privacy Policy</a>
                            </span>
                        </div>
                        <div id="createAccountButton" class="button">
                            <span class="fieldHeader">Create Account</span>
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