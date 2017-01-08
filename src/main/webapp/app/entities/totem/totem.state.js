(function() {
    'use strict';

    angular
        .module('traveltotemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('totem', {
            parent: 'entity',
            url: '/totem',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'traveltotemApp.totem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/totem/totems.html',
                    controller: 'TotemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('totem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('totem-detail', {
            parent: 'entity',
            url: '/totem/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'traveltotemApp.totem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/totem/totem-detail.html',
                    controller: 'TotemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('totem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Totem', function($stateParams, Totem) {
                    return Totem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'totem',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('totem-detail.edit', {
            parent: 'totem-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/totem/totem-dialog.html',
                    controller: 'TotemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Totem', function(Totem) {
                            return Totem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('totem.new', {
            parent: 'totem',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/totem/totem-dialog.html',
                    controller: 'TotemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                creationLatitude: null,
                                creationLongitude: null,
                                creationDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('totem', null, { reload: 'totem' });
                }, function() {
                    $state.go('totem');
                });
            }]
        })
        .state('totem.edit', {
            parent: 'totem',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/totem/totem-dialog.html',
                    controller: 'TotemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Totem', function(Totem) {
                            return Totem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('totem', null, { reload: 'totem' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('totem.delete', {
            parent: 'totem',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/totem/totem-delete-dialog.html',
                    controller: 'TotemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Totem', function(Totem) {
                            return Totem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('totem', null, { reload: 'totem' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
