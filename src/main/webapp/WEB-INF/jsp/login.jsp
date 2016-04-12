<%@include file="includes.jsp" %>
<div class="clearboth tenspacer">&nbsp;</div>
<div id="loginbox">
    <div class="col-md-4 col-md-offset-4">
        <div class="text-center">
            <img src="<c:url value="/images/spider_logo_small.png" />" />
            <h1>Site Scraper Login</h1><br />
        </div>      

        <div class="panel panel-default">
            <div class="panel-body">
                <c:if test="${param.error != null}">
                    <div class="alert alert-danger text-center"><strong>Authentication Failed.</strong>
                        </br>
                        <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
                    </div>
                </c:if>
                <c:if test="${param.logout != null}">
                    <div class="alert alert-success text-center">You have been logged out.</div>
                </c:if>
                <form id="loginform" role="form" action="<c:url value="/login" />" method="post">
                    <div class="form-group">
                        <label for="username" class="control-label">Username:</label>
                        <input type="text" class="form-control" id="username" name="username" placeholder="Username" />
                    </div>
                    <div class="form-group">
                        <label for="password" class="control-label">Password:</label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Password" />
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button id="loginbutton" type="submit" class="btn btn-warning btn-block">Login</button>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="clearboth tenspacer">&nbsp;</div>