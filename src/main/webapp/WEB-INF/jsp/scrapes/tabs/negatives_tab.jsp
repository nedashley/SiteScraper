<div>
    <div class="panel panel-default">
        <div class="panel-heading">
            <div class="clearfix">
                <h4>Scrape Negatives</h4>
            </div>
        </div>
        <div class="panel-body">
            <div class="col-md-12">
                <div class="clearfix">
                    <table class="table">
                        <thead>
                            <tr class="danger">
                                <th class="col-md-4">Url</th>
                                <th class="col-md-2">Started</th>
                                <th class="col-md-2">Finished</th>
                                <th class="col-md-2">Resolves</th>
                                <th class="col-md-2">Response</th>
                                <!--<th></th>-->
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="link in model.scrape.links | filter:{successful:false}:true">
                                <td><a ng-href="{{link.url}}" target="_blank">{{link.url}}</a></td>
                                <td>{{link.started|date:'EEE, dd MMM yyyy, hh:mm a'}}</td>
                                <td>{{link.finished|date:'EEE, dd MMM yyyy, hh:mm a'}}</td>
                                <td>{{link.resolves}}</td>
                                <td>{{link.response}}</td>
                                <!--<td class="text-right">
                                    <button type="button" class="btn btn-sm btn-default" ng-click="viewEmails($index)"><i class="fa fa-envelope"></i> Emails</button>
                                </td>-->
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>