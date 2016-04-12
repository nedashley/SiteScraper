var siteScraperApp = angular.module('SiteScraperApp',
        [
            'ui.bootstrap',
            'ngRoute',
            'ngResource',
            'ngSanitize'
        ]);

siteScraperApp.config(['$routeProvider', '$locationProvider',
    function ($routeProvider, $locationProvider) {
        $routeProvider.
                when('/scrapes/overview', {
                    templateUrl: '/SiteScraper/scrapes/overview.htm',
                    controller: 'ScrapesOverviewController'
                }).
                when('/scrapes/:scrapeId', {
                    templateUrl: '/SiteScraper/scrapes/scrape.htm',
                    controller: 'ScrapeController'
                }).
                otherwise({
                    redirectTo: '/scrapes/overview'
                });
        $locationProvider
                .html5Mode(false)
                .hashPrefix('!');
    }]);

siteScraperApp.service('ScrapeApiService', ['$http', function ($http) {
        this.lookupOverview = function () {
            return $http.get('/SiteScraper/scrapes/overview.json');
        };
        this.lookupScrape = function (id) {
            return $http.get('/SiteScraper/scrapes/' + id + '.json');
        };
        this.createScrape = function (model) {
            return $http.post('/SiteScraper/scrapes/create.json', model);
        };
    }]);

siteScraperApp.factory('AppAlert', ['$rootScope', '$timeout',
    function ($rootScope, $timeout) {
        var alertService;
        $rootScope.$on('alert', function (event, arguments) {
            try {
                if ((typeof arguments !== 'undefined') && (arguments)) {
                    alertService.add('warning', arguments);
                }
            } catch (error) {
            }
        });
        $rootScope.alerts = [];
        return alertService = {
            add: function (type, msg, timeout) {
                $rootScope.alerts.length = 0;
                var alert = {
                    type: type,
                    msg: msg,
                    close: function () {
                        alertService.closeAlert(this);
                    }
                };
                $rootScope.alerts.push(alert);
                if (timeout) {
                    $timeout(function () {
                        alertService.closeAlert(alert);
                    }, timeout);
                } else {
                    $timeout(function () {
                        alertService.closeAlert(alert);
                    }, 5000);
                }
            },
            closeAlert: function (alert) {
                if ($rootScope.alerts.indexOf(alert) !== -1) {
                    this.closeAlertIdx($rootScope.alerts.indexOf(alert));
                }
            },
            closeAlertIdx: function (index) {
                $rootScope.alerts.splice(index, 1);
            },
            clear: function () {
                $rootScope.alerts.length = 0;
            }
        };
    }
]);

siteScraperApp.controller('ScrapesOverviewController', ['$scope', '$window', '$uibModal', '$location', '$q', 'ScrapeApiService', 'AppAlert',
    function ($scope, $window, $uibModal, $location, $q, scrapeApiService, appAlert) {
        $scope.model = {
            scrapes: []
        };
        $scope.createScrape = function () {
            var modalInstance = $uibModal.open({
                templateUrl: '/SiteScraper/scrapes/modals/create_scrape_modal.htm',
                controller: 'CreateScrapeModalInstanceController',
                backdrop: 'static',
                keyboard: true,
                resolve: {}
            });
            modalInstance.result.then(function (scrapes) {
                $scope.model.scrapes = scrapes;
            }, function (message) {
            });
        };
        $scope.selectScrape = function ($event, scrape) {
            $event.preventDefault();
            $event.stopPropagation();
            $location.path('/scrapes/' + scrape.id).search({});
        };
        $scope.viewScrapeEmails = function (scrape) {
            var modalInstance = $uibModal.open({
                templateUrl: '/SiteScraper/scrapes/modals/emails_modal.htm',
                controller: 'EmailsModalInstanceController',
                backdrop: 'static',
                keyboard: true,
                resolve: {
                    links: function () {
                        var links = [{
                                url: '',
                                emails: scrape.emails
                            }];
                        return links;
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function (message) {
            });
        };
        $scope.viewScrapeReport = function (scrape) {
            $window.open('/SiteScraper/scrapes/' + scrape.id + '/report.xlsx');
        };
        $scope.loadScrapes = function () {
            scrapeApiService.lookupOverview().success(function (data) {
                $scope.model.scrapes = data;
            }).error(function (data, status, headers, config) {
                appAlert.add("danger", "Error occurred loading Scrapes: " + (typeof (data.message) !== 'undefined' ? data.message : status));
                $scope.model.scrapes = [];
            });
        };
        $scope.loadScrapes();
    }]);

siteScraperApp.controller('CreateScrapeModalInstanceController', ['$scope', '$uibModalInstance', 'ScrapeApiService', 'AppAlert',
    function ($scope, $uibModalInstance, scrapeApiService, appAlert) {
        $scope.model = {
            scrape: {
                name: '',
                maxDepth: 1,
                urls: ''
            }
        };
        $scope.finish = function () {
            scrapeApiService.createScrape($scope.model.scrape).success(function (data) {
                $uibModalInstance.close(data);
            }).error(function (data, status, headers, config) {
                appAlert.add("danger", "Error creating Scrape: " + (typeof (data.message) !== 'undefined' ? data.message : status));
            });
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.finishDisabled = function () {
            return (!
                    ((angular.isDefined($scope.model.scrape)) && ($scope.model.scrape !== null) &&
                            (angular.isDefined($scope.model.scrape.name)) && ($scope.model.scrape.name !== null)
                            && (angular.isDefined($scope.model.scrape.urls)) && ($scope.model.scrape.urls !== null)
                            && (angular.isDefined($scope.model.scrape.maxDepth)) && ($scope.model.scrape.maxDepth > 0) && ($scope.model.scrape.maxDepth < 4)));
        };
    }]);

siteScraperApp.controller('EmailsModalInstanceController', ['$scope', '$uibModalInstance', 'ScrapeApiService', 'AppAlert', 'links',
    function ($scope, $uibModalInstance, scrapeApiService, appAlert, links) {
        $scope.model = {
            links: links
        };
        $scope.finish = function () {
            $uibModalInstance.close();
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);

siteScraperApp.controller('InnerLinksModalInstanceController', ['$scope', '$uibModalInstance', 'ScrapeApiService', 'AppAlert', 'innerLinks',
    function ($scope, $uibModalInstance, scrapeApiService, appAlert, innerLinks) {
        $scope.model = {
            innerLinks: innerLinks
        };
        $scope.finish = function () {
            $uibModalInstance.close();
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);

siteScraperApp.controller('ScrapeController', ['$scope', '$window', '$uibModal', '$location', '$routeParams', 'ScrapeApiService', 'AppAlert',
    function ($scope, $window, $uibModal, $location, $routeParams, scrapeApiService, appAlert) {
        $scope.model = {
            scrapeId: $routeParams.scrapeId,
            scrape: null
        };
        $scope.viewLinkEmails = function (link) {
            var modalInstance = $uibModal.open({
                templateUrl: '/SiteScraper/scrapes/modals/emails_modal.htm',
                controller: 'EmailsModalInstanceController',
                backdrop: 'static',
                keyboard: true,
                resolve: {
                    links: function () {
                        var links = [link];
                        return links;
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function (message) {
            });
        };
        $scope.viewInnerLinks = function (link) {
            var modalInstance = $uibModal.open({
                templateUrl: '/SiteScraper/scrapes/modals/inner_links_modal.htm',
                controller: 'InnerLinksModalInstanceController',
                backdrop: 'static',
                keyboard: true,
                size: 'lg',
                resolve: {
                    innerLinks: function () {
                        return link.innerLinks;
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function (message) {
            });
        };
        $scope.loadScrape = function () {
            scrapeApiService.lookupScrape($scope.model.scrapeId).success(function (data) {
                $scope.model.scrape = data;
            }).error(function (data, status, headers, config) {
                appAlert.add("danger", "Error occurred loading Scrape: " + (typeof (data.message) !== 'undefined' ? data.message : status));
                $scope.model.scrape = null;
            });
        };
        $scope.loadScrape();
    }]);