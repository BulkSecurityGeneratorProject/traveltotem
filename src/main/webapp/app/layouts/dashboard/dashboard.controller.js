(function() {
    'use strict';

    angular
        .module('traveltotemApp')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function DashboardController ($state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;


    }
})();
