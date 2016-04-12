<%@include file="../includes.jsp" %>
<div class="content">
    <div class="col-md-12">
        <div id="scrape-details-panel">
            <div class="well well-sm">
                <div class="clearfix form-horizontal">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label col-md-3">Name:</label>
                            <div class="col-md-8">
                                <p class="form-control-static">{{model.scrape.name}}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">Started:</label>
                            <div class="col-md-8">
                                <p class="form-control-static">{{model.scrape.started|date:'EEE, dd MMM yyyy, hh:mm a'}}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">Finished:</label>
                            <div class="col-md-8">
                                <p class="form-control-static">{{model.scrape.finished|date:'EEE, dd MMM yyyy, hh:mm a'}}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label col-md-3">URLS:</label>
                            <div class="col-md-8">
                                <p class="form-control-static">{{model.scrape.links.length}}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">Successful:</label>
                            <div class="col-md-8">
                                <p class="form-control-static">{{model.scrape.successfulLinks.length}}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-3">Failed:</label>
                            <div class="col-md-8">
                                <p class="form-control-static">{{model.scrape.failedLinks.length}}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="scrape-details-tabs">
                <uib-tabset vertical="false">
                    <uib-tab>
                        <uib-tab-heading>Successful</uib-tab-heading>
                            <tiles:insertAttribute name="positives_tab" />
                    </uib-tab>
                    <uib-tab>
                        <uib-tab-heading>Failed</uib-tab-heading>
                            <tiles:insertAttribute name="negatives_tab" />
                    </uib-tab>
                </uib-tabset>
            </div>
        </div>
        <div class="clearfix"></div>
    </div>
</div>
