/*
 * This software is provided AS IS without any warranty for any kind of use. Author is not liable for any loss for using
 * this software.
 * https://codepen.io/m-e-conroy/pen/ALsdF
 */

(function (angular, undefined) {"use strict";
    var app = angular.module("chartDataApp", ['ngMaterial', 'ui.router', 'ngRoute', 'ngMessages']);

    app.config(function($stateProvider) {
        $stateProvider
            .state('transit', {
                url: '',
                data: {
                    'selectedTab': 0
                },
                templateUrl: 'transit.html'
            })
            .state('d9', {
                url: '',
                data: {
                    'selectedTab': 1
                },
                templateUrl: 'd9.html'
            });
    });
    
    var chartDataService = function($http) {
        var cds = {};
        
        cds.chartData = {};
        cds.transitData = {};
        cds.sublords = [];
        cds.grid = [];
        cds.transitGrid = [];
        
        cds.dataChangeEvent = function() {
            return cds.chartData;
        };
        
        cds.setData = function(chartData) {
            if (chartData === null) {
                console.log("HTTP Get !!");
                $http.get("/rest/ws/jathakamService")
                    .then(onComplete, onError);
            } else {
                console.log("HTTP Post")
                $http.post("/rest/ws/jathakamService", chartData)
                    .then(onComplete, onError);
            }
            
            function onComplete(response) {
                if (angular.isUndefined(cds.chartData.name)) {
                    cds.transitData = response.data;
                    cds.transitGrid = arrangeGrid(cds.transitData);
                }
                cds.chartData = response.data;
                cds.message = "Chart calculated";
                cds.errorCode = 0;
                console.log("calling calculateSubLords");
                calculateSubLords();
                cds.grid = arrangeGrid(cds.chartData);
            }

            function onError(reason) {
                cds.message = "Could not calculate chart info";
                cds.errorCode = 1;
                console.log(reason);
            }
        };
        
        /* convenience methods */
        cds.getPlanetLongitude = function(planetIndex) {
            var i, j;

            for (i = 0; i < 12; i++) {
                var info = cds.chartData.zodiacInfo[i].infoList;

                for (j = 0; j < info.length; j++) {
                    var pos = info[j].position;
                    var type = info[j].objectType;
                    
                    if (type === planetIndex) {
                        return pos;
                    }
                }
            }
            
            return null;
        };
        
        cds.getChartTime = function() {
            return new Date(cds.chartData.year, cds.chartData.month, cds.chartData.day,
                cds.chartData.hours, cds.chartData.minutes, cds.chartData.seconds, cds.chartData.millis);
        };

        /* Private functions */

        /* To calculate sub lords */
        function calculateSubLords() {
            console.log("calculateSubLords called");
            cds.sublords = [];
            var i, j;
            for (i = 0; i < 12; i++) {
                var info = cds.chartData.zodiacInfo[i].infoList;

                for (j = 0; j < info.length; j++) {
                    var pos = info[j].position;
                    var type = info[j].objectType;
                    var calc = new Calc(pos);
                    var sublords = calc.lords();
                    var sl = PLANETS[sublords[0].planet].name;
                    var nl = PLANETS[sublords[1].planet].name;
                    var subl = PLANETS[sublords[2].planet].name;
                    var sub2l = PLANETS[sublords[3].planet].name;

                    cds.sublords.push({objectType: type, position: pos, name: objectSign(type), raasiLord: sl,
                        starLord: nl, sublord: subl, sub2lord: sub2l});
                }
            }
        };
        /* To show natal chart in south india style. returns tile info for angular material md-grid-list */
        function arrangeGrid(dataValues) {
            var grid = [];
            var zodiacSigns = ['', '\u2648', '\u2649', '\u264A', '\u264B', '\u264C', '\u264D', '\u264E', '\u264F', '\u2650',
                                '\u2651', '\u2652', '\u2653'];
            var zodiacIndex = [11, 0, 1, 2, 10, -1, 3, 9, 4, 8, 7, 6, 5];
            var i = 0;

            for (;i < zodiacIndex.length; i++) {
                var ind = zodiacIndex[i];
                var zi = {
                    pos: ind,
                    sign: zodiacSigns[ind+1],
                    span: { row: 1, col: 1}
                };
                
                if (ind === -1) {
                    zi.infoList = [];
                    zi.span= { row: 2, col: 2};
                }
                else {
                    zi.infoList = dataValues.zodiacInfo[ind].infoList;
                }
                
                grid.push(zi);
            }
            
            return grid;
        }
        
        return cds;
    };

    var MainController = function ($scope, chartDataSrv) {
        console.log("MainController initialized")
        var self = this;
        
        self.chartData = {};
        self.transitData = {};
        self.sublords = {};
        self.message = "";
        self.errorCode = "";
        self.degree = degree;
        self.objectSign = objectSign;
        self.grid = [];
        self.transitGrid = [];
        
        chartDataSrv.setData(null);
        
        self.onChartSubmit = function () {
            chartDataSrv.setData(self.chartData);
        };
        
        $scope.$watch(chartDataSrv.dataChangeEvent, function(newValue, oldValue) {
            if (newValue !== oldValue && angular.isDefined(newValue)) {
                self.chartData = chartDataSrv.chartData;
                self.timeOfChart = new Date(chartDataSrv.chartData.year, chartDataSrv.chartData.month-1, chartDataSrv.chartData.day,
                            chartDataSrv.chartData.hours, chartDataSrv.chartData.minutes, chartDataSrv.chartData.seconds,
                            chartDataSrv.chartData.millis);
                self.grid = chartDataSrv.grid;
            }
        });
        
        $scope.$watch(function() { return chartDataSrv.sublords; }, function(newValue, oldValue) {
            if (newValue !== oldValue && angular.isDefined(newValue)) {
                self.sublords = chartDataSrv.sublords;
            }
        });
        
        $scope.$watch(function() { return chartDataSrv.transitData; }, function(newValue, oldValue) {
            if (newValue !== oldValue && angular.isDefined(newValue)) {
                self.transitData = chartDataSrv.transitData;
                self.transitGrid = chartDataSrv.transitGrid;
                self.transitTimeOfChart = new Date(chartDataSrv.chartData.year, chartDataSrv.chartData.month-1, chartDataSrv.chartData.day,
                            chartDataSrv.chartData.hours, chartDataSrv.chartData.minutes, chartDataSrv.chartData.seconds,
                            chartDataSrv.chartData.millis);
            }
        });
    };
    
    var DivisonalChartsController = function ($scope, $state, chartDataSrv) {
        console.log("initializing DivisonalChartsController - " + $state);
        var self = this;
        
        self.grid = setDivisionalChart(chartDataSrv.chartData);
        
        $scope.$watch(chartDataSrv.dataChangeEvent, function(newValue, oldValue) {
            if (newValue !== oldValue && angular.isDefined(newValue)) {
                console.log("DivisonalChartsController chartData changed");
                self.grid = setDivisionalChart(newValue);
                $state.reload();
            }
        });
        
        function setDivisionalChart(dataValues) {
            console.log("dataValues = " + dataValues);
            
            var grid = [];
            var zodiacSigns = ['', '\u2648', '\u2649', '\u264A', '\u264B', '\u264C', '\u264D', '\u264E', '\u264F', '\u2650',
                                '\u2651', '\u2652', '\u2653'];
            var zodiacIndex = [11, 0, 1, 2, 10, -1, 3, 9, 4, 8, 7, 6, 5];
            var i = 0;

            for (;i < zodiacIndex.length; i++) {
                var ind = zodiacIndex[i];
                var zi = {
                    pos: ind,
                    sign: zodiacSigns[ind+1],
                    span: { row: 1, col: 1},
                    infoList: []
                };
                
                if (ind === -1) {
                    zi.span= { row: 2, col: 2};
                }
                else {
                    var j = 0;
                    var list = dataValues.zodiacInfo[ind].infoList;
                    var n = list.length;
                    for (;j < n; j++) {
                        var info = list[j];
                        console.log("info = " + info + ", position = " + info.position + ", sign = " + info.objectType);
                        var newInfo = {
                            position: degree(info.position + 10),
                            objectType: objectSign(info.objectType)
                        };
                        zi.infoList.push(newInfo);
                    }
                }
                
                grid.push(zi);
            }
            
            return grid;
        }
    };

    var VimshottariDasaController = function($scope, chartDataSrv) {
        var self = this;

        var mahaDasa = [];
        var selectedMaha; // level 1
        var selectedBhukti; // level 2
        var selectedPratyantara; // level 3
        var selectedSookshma; // level 4

        self.dasas = [];
        self.level = 1;

        $scope.$watch(chartDataSrv.dataChangeEvent, function(newValue, oldValue) {
            if (newValue !== oldValue && angular.isDefined(newValue)) {
                console.log("VimshottariDasaController chartData changed");
                mahaDasa = getDasas();
                self.dasas = mahaDasa;
                self.level = 1;
            }
        });
        
        function getDasas() {
            var longitude = chartDataSrv.getPlanetLongitude(2); // Moon
            var chartTime = chartDataSrv.getChartTime();
            var vdc = new VdCalc(chartTime.getTime(), longitude);
            var dasaList = vdc.mahadasa();

            return dasaList;
        }

        self.nextDasa = function(selectedDasa) {
            if (self.level > 4) {
                self.level = 5;
                return;
            }

            switch (self.level) {
                case 1:
                    selectedMaha = selectedDasa;
                    break;
                case 2:
                    selectedBhukti = selectedDasa;
                    break;
                case 3:
                    selectedPratyantara = selectedDasa;
                    break;
                case 4:
                    selectedSookshma = selectedDasa;
                    break;
            }

            self.dasas = selectedDasa.subDasa();

            self.level++;
        };

        self.previous = function() {
            if (self.level < 2) {
                self.level = 1;
                return;
            }

            self.level--;

            switch (self.level) {
                case 1:
                    self.dasas = mahaDasa;
                    break;
                case 2:
                    self.dasas = selectedMaha.subDasa();
                    break;
                case 3:
                    self.dasas = selectedBhukti.subDasa();
                    break;
                case 4:
                    self.dasas = selectedPratyantara.subDasa();
                    break;
            }
        };
    };
    
    function MenuController() {
        var self = this;
        var originatorEv;
        
        self.openMenu = function($mdOpenMenu, ev) {
            originatorEv = ev;
            $mdOpenMenu(ev);
        };
        
        self.enableButton = "disabled";
    }
    
    function DialogController($mdDialog, chartDataSrv) {
        var self = this;
        
        self.name = chartDataSrv.chartData.name;
        self.dateTime = new Date(chartDataSrv.chartData.year, chartDataSrv.chartData.month-1, chartDataSrv.chartData.day,
                            chartDataSrv.chartData.hours, chartDataSrv.chartData.minutes, chartDataSrv.chartData.seconds,
                            chartDataSrv.chartData.millis);
        self.placeName = chartDataSrv.chartData.placeName;
        self.longitude = chartDataSrv.chartData.longitude * 1.0;
        self.latitude = chartDataSrv.chartData.latitude * 1.0;
        self.maxDate = new Date();
        
        self.hide = function() {
            $mdDialog.hide();
        };
        
        self.cancel = function() {
            console.log("Cancel clicked");
            $mdDialog.cancel();
        };
        
        self.save = function(answer) {
            console.log("chartDataSrv.chartData.month = " + chartDataSrv.chartData.month
                    + ", self.dateTime.getMonth() = " + self.dateTime.getMonth());
            chartDataSrv.chartData.name = self.name;
            chartDataSrv.chartData.year = self.dateTime.getFullYear();
            chartDataSrv.chartData.month = self.dateTime.getMonth()+1;
            chartDataSrv.chartData.day = self.dateTime.getDate();
            chartDataSrv.chartData.hours = self.dateTime.getHours();
            chartDataSrv.chartData.minutes = self.dateTime.getMinutes();
            chartDataSrv.chartData.seconds = self.dateTime.getSeconds();
            chartDataSrv.chartData.millis = self.dateTime.getMilliseconds();
            chartDataSrv.chartData.placeName = self.placeName;
            chartDataSrv.chartData.longitude = self.longitude;
            chartDataSrv.chartData.latitude = self.latitude;
            chartDataSrv.setData(chartDataSrv.chartData);
            $mdDialog.hide(answer);
            console.log("Save clicked");
        };
        
        self.showChartInfo = function(ev) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: 'chart-dialog-tmpl.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose:true
            })
                .then(function(answer) {
                    self.message = 'You said the information was "' + answer + '".';
                    console.log(self.message);
                }, function() {
                    self.message = 'You cancelled the dialog.';
                    console.log(self.message);
            });
        };
    }

    app.factory("chartDataSrv", ["$http", chartDataService])
       .controller("MainController", ["$scope", "chartDataSrv", MainController])
       .controller("DivisonalChartsController", ["$scope", "$state", "chartDataSrv", DivisonalChartsController])
       .controller("VimshottariDasaController", ["$scope", "chartDataSrv", VimshottariDasaController])
       .controller("MenuController", [MenuController])
       .controller("DialogController", ["$mdDialog", "chartDataSrv", DialogController]);
}(angular));
