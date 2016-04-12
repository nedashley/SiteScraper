<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../includes.jsp" %>
<!DOCTYPE HTML>
<html>
    <head>
        <base href="/SiteScraper/">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><tiles:insertAttribute name="page_title" ignore="true" /> - Site Scraper</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="<c:url value="/css/bootstrap.min.css" />" rel="stylesheet" type="text/css" />
        <link href="<c:url value="/css/font-awesome.min.css" />" rel="stylesheet" type="text/css" />
        <link href="<c:url value="/css/style.css" />" rel="stylesheet" type="text/css" />
        <style>
            /* Hide Angular JS elements before initializing */
            .ng-cloak {
                display: none;
            }
        </style>
    </head>
    <body ng-app="SiteScraperApp">
        <noscript>
        <style type="text/css">
            .pagecontainer {display:none;}
        </style>
        <div class="noscriptmsg">
            You don't have javascript enabled, please enable it to use this site.
        </div>
        </noscript>
        <div id="notifications">
            <uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="alert.close()">{{alert.msg}}</uib-alert>
        </div>

        <tiles:insertAttribute name="navbar" />
        <div id="wrapper">
            <div class="content">
                <div class="col-md-12" ng-view></div>
            </div>
            <div class="clearfix"></div>
        </div>

        <script type="text/javascript" src="<c:url value="/js/moment.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/angular-1.4.8/angular.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/angular-1.4.8/angular-animate.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/angular-1.4.8/angular-aria.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/angular-1.4.8/angular-cookies.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/angular-1.4.8/angular-loader.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/angular-1.4.8/angular-message-format.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/angular-1.4.8/angular-messages.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/angular-1.4.8/angular-resource.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/angular-1.4.8/angular-route.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/angular-1.4.8/angular-sanitize.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/angular-1.4.8/angular-touch.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/ui-bootstrap-tpls-0.14.3.js" />"></script>
        <sec:authorize access="isAuthenticated()">
            <script type="text/javascript" src="<c:url value="/js/site-scraper-app.js" />"></script>
        </sec:authorize>

    </body>
</html>
