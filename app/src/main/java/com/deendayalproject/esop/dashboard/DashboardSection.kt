package com.deendayalproject.esop.dashboard






import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.deendayalproject.R
import kotlin.String

@Composable
fun DashboardSection(
    navController: NavController,
    candidateLoginEmail: String,
    candidateLoginId: String,
    candidateName: String,
    candidateMobileNo: String
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth()
    )
    {
            DashboardCard(
                modifier = Modifier.fillMaxWidth(),
                title = "My Tests",
                subtitle = "View your tests",
                icon = Icons.Default.Assignment,
                onClick = {
                    val bundle = bundleOf(
                        "candidateLoginEmail" to candidateLoginEmail,
                        "candidateLoginId" to candidateLoginId
                    )
                    navController.navigate(R.id.action_esopFragment_to_esopTestFragment,bundle)

                },
                candidateName=candidateName,
                candidateMobileNo=candidateMobileNo
            )

        Spacer(modifier = Modifier.height(12.dp))

        DashboardCard(
            modifier = Modifier.fillMaxWidth(),
            title = "Results",
            subtitle = "View your results",
            icon = Icons.Default.BarChart,
            onClick = {
                val bundle = bundleOf(
                    "candidateLoginEmail" to candidateLoginEmail,
                    "candidateLoginId" to candidateLoginId,
                    "candidateName" to candidateName,
                    "candidateMobileNo" to candidateMobileNo
                )

                navController.navigate(R.id.action_esopFragment_to_esopResultFragment,bundle)


//                Toast.makeText(
//                    context,
//                    "Results",
//                    Toast.LENGTH_SHORT
//                ).show()
            },
            candidateName=candidateName,
            candidateMobileNo=candidateMobileNo
        )

//        Spacer(modifier = Modifier.height(12.dp))
//
//        DashboardCard(
//            modifier = Modifier.fillMaxWidth(),
//            title = "Certificate",
//            subtitle = "View & Download",
//            icon = Icons.Default.CardMembership,
//            onClick = {
//
//                Toast.makeText(
//                    context,
//                    "Certificate",
//                    Toast.LENGTH_SHORT
//                ).show()
//            },
//            candidateName=candidateName,
//            candidateMobileNo=candidateMobileNo
//        )
    }

}
//      private fun navigate(id: Int) {
//    findNavController().navigate(id)
//      }
