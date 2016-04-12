<%@include file="includes.jsp" %>
<div id="header">
    <div class="color-line"></div>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button> 
                <a class="navbar-brand" href="<c:url value='/scrapes/' />">
                    <img src="<c:url value="/images/spider_logo_small.png" />" height="50" />
                    <span>Site Scraper</span>
                </a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

                <ul class="nav navbar-nav">
                    <sec:authorize access="hasAnyRole('SALES_TEAM','INTRODUCER')">
                        <li uib-dropdown>
                            <a href="/PanelManagement/sales_leads/#!/sales_leads/overview" id="sales-leads-dropdown" uib-dropdown-toggle>Leads &nbsp; <span class="caret"></span></a>
                            <ul uib-dropdown-menu aria-labelledby="sales-leads-dropdown">
                                <li><a href="/PanelManagement/sales_leads/#!/sales_leads/overview" >Sales Leads</a></li>    
                                <li><a href="/PanelManagement/sales_leads/#!/sales_leads/search" >Search</a></li>    
                                <li><a href="/PanelManagement/sales_leads/#!/sales_leads/create" >New Lead</a></li>    
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>

                <form class="navbar-right navbar-form" action="<c:url value='/logout.htm' />" method="post">
                    <!-- <button type="submit" class="btn btn-default"></button> -->
                    <a href="" onclick="document.forms[0].submit();
                            return false;"><i class="fa fa-sign-out"></i> Logout</a> 
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div><!--/.container-fluid -->
    </nav>
</div>