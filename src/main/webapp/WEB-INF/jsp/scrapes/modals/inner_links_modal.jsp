<div id="inner-links-modal">
    <div class="modal-header text-primary"><strong>Inner Links</strong></div>
    <div class="modal-body">
        <div style="max-height: 500px; overflow-y: auto;">
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Depth</th>
                        <th>URL</th>
                        <th>Started</th>
                        <th>Finished</th>
                        <th>Response</th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="link in model.innerLinks">
                        <td>{{link.depth}}</td>
                        <td>{{link.url}}</td>
                        <td>{{link.started|date:'EEE, dd MMM yyyy, hh:mm a'}}</td>
                        <td>{{link.finished|date:'EEE, dd MMM yyyy, hh:mm a'}}</td>
                        <td>{{link.response}}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="modal-footer">
        <div class="btn-group pull-right">
            <button type="button" class="btn btn-default btn-sm" ng-click="cancel()"><i class="fa fa-times"></i> Close</button>
        </div>
    </div>
</div>