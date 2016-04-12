<%@include file="../includes.jsp" %>
<div class="content">
    <div class="col-md-12">
        <div id="scrapes-overview-panel">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h4>Overview</h4>
                        <small>Lists all scrapes</small>
                    </div>
                    <div class="pull-right">
                        <div class="btn-group">
                            <button type="button" class="btn btn-default" ng-click="loadScrapes()"><i class="fa fa-refresh"></i> Refresh</button>
                            <button type="button" class="btn btn-warning" ng-click="createScrape()"><i class="fa fa-plus"></i> New Scrape</button>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="panel-body">
                    <div class="col-md-12">
                        <table class="table table-condensed table-bordered table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th class="text-center">Max Depth</th>
                                    <th class="text-center">Started</th>
                                    <th class="text-center">Finished</th>
                                    <th class="text-center">URLs</th>
                                    <th class="text-center">Successful</th>
                                    <th class="text-center">Failed</th>
                                    <th class="text-center">Emails</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr ng-repeat="scrape in model.scrapes">
                                    <td>{{scrape.name}}</td>
                                    <td class="text-center">{{scrape.maxDepth}}</td>
                                    <td class="text-center">{{scrape.started|date:'EEE, dd MMM yyyy, hh:mm a'}}</td>
                                    <td class="text-center">{{scrape.finished|date:'EEE, dd MMM yyyy, hh:mm a'}}</td>
                                    <td class="text-center">{{scrape.linkCount}}</td>
                                    <td class="text-center">{{scrape.positivesCount}}</td>
                                    <td class="text-center">{{scrape.negativesCount}}</td>
                                    <td class="text-center">{{scrape.emails.length}}</td>
                                    <td class="text-right">
                                        <button type="button" class="btn btn-sm btn-default" ng-click="selectScrape($event, scrape)"><i class="fa fa-sitemap"></i> View</button>
                                        <button type="button" class="btn btn-sm btn-default" ng-click="viewScrapeEmails(scrape)"><i class="fa fa-envelope"></i> Emails</button>
                                        <button type="button" class="btn btn-sm btn-default" ng-click="viewScrapeReport(scrape)"><i class="fa fa-file-excel-o"></i> Report</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="clearfix">&nbsp;</div>
    </div>
    <div class="clearfix"></div>
</div>
