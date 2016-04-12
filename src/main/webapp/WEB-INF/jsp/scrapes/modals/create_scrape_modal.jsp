<div id="create-scrape-modal">
    <div class="modal-header text-primary"><strong>Create Scrape</strong></div>
    <div class="modal-body">
        <form role="form" class="form-horizontal">
            <fieldset>
                <div class="form-group">
                    <label for="name" class="col-md-3 control-label">Name:</label>
                    <div class="col-md-8">
                        <input type="text" id="name" class="form-control" ng-model="model.scrape.name" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="maxDepth" class="col-md-3 control-label">Max Depth:</label>
                    <div class="col-md-2">
                        <input type="number" id="maxDepth" class="form-control" ng-model="model.scrape.maxDepth" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="urls" class="col-md-3 control-label">URL's:</label>
                    <div class="col-md-8">
                        <textarea id="urls" class="form-control" ng-model="model.scrape.urls" rows="6"></textarea>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
    <div class="modal-footer">
        <div class="btn-group pull-left">
            <button type="button" class="btn btn-default btn-sm" ng-click="cancel()"><i class="fa fa-times"></i> Cancel</button>
        </div>
        <div class="btn-group pull-right">
            <button type="button" class="btn btn-warning btn-sm" ng-click="finish()" ng-disabled="finishDisabled()">Create</button>
        </div>
    </div>
</div>
