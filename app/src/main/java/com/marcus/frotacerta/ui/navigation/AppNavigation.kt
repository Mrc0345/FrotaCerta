package com.marcus.frotacerta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marcus.frotacerta.FrotaCertaApplication
import com.marcus.frotacerta.ui.dashboard.DashboardScreen
import com.marcus.frotacerta.ui.vehicle.VehicleFormScreen
import com.marcus.frotacerta.ui.vehicle.VehicleListScreen
import com.marcus.frotacerta.ui.vehicle.VehicleViewModel
import com.marcus.frotacerta.ui.vehicle.VehicleViewModelFactory

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val context = LocalContext.current

    val application =
        context.applicationContext as FrotaCertaApplication

    val vehicleViewModel: VehicleViewModel = viewModel(
        factory = VehicleViewModelFactory(
            application.container.vehicleRepository
        )
    )

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {

        composable(
            route = Routes.DASHBOARD
        ) {

            DashboardScreen(
                onOpenVehicles = {
                    navController.navigate(Routes.VEHICLES)
                },
                onNewRental = {
                    // Tela de nova locação será implementada na próxima etapa.
                }
            )
        }

        composable(
            route = Routes.VEHICLES
        ) {

            VehicleListScreen(
                viewModel = vehicleViewModel,
                onAddVehicle = {
                    navController.navigate(Routes.NEW_VEHICLE)
                }
            )
        }

        composable(
            route = Routes.NEW_VEHICLE
        ) {

            VehicleFormScreen(
                viewModel = vehicleViewModel,
                onVehicleSaved = {
                    navController.popBackStack()
                }
            )
        }
    }
}
