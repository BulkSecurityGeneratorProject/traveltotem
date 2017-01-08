(function() {
    'use strict';
    angular
        .module('traveltotemApp')
        .factory('Totem', Totem);

    Totem.$inject = ['$resource', 'DateUtils'];

    function Totem ($resource, DateUtils) {
        var resourceUrl =  'api/totems/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertLocalDateFromServer(data.creationDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.creationDate = DateUtils.convertLocalDateToServer(copy.creationDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.creationDate = DateUtils.convertLocalDateToServer(copy.creationDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
