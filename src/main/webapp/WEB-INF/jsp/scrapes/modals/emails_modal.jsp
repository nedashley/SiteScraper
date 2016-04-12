<div id="emails-modal">
    <div class="modal-header text-primary"><strong>Emails</strong></div>
    <div class="modal-body">
        <div style="max-height: 500px; overflow-y: auto;">
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Site</th>
                        <th>Email</th>
                    </tr>
                </thead>
                <tbody ng-repeat="link in model.links">
                    <tr ng-repeat="email in link.emails">
                        <td>{{link.url}}</td>
                        <td>{{email}}</td>
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