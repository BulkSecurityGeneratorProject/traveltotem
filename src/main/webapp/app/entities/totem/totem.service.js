(function() {
    'use strict';
    angular
        .module('traveltotemApp')
        .factory('Totem', Totem);

    Totem.$inject = ['$resource'];

    function Totem ($resource) {
        var resourceUrl =  'api/totems/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
