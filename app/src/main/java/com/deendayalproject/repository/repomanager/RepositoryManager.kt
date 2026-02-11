package com.deendayalproject.repository.repomanager

import android.content.Context
import com.deendayalproject.repository.AcademicRepository
import com.deendayalproject.repository.AuthRepository
import com.deendayalproject.repository.FieldVerificationRepository
import com.deendayalproject.repository.InfrastructureRepository
import com.deendayalproject.repository.LocationRepository
import com.deendayalproject.repository.ResidentialFacilityRepository
import com.deendayalproject.repository.RfOperationsRepository
import com.deendayalproject.repository.TrainingCenterRepository
import com.deendayalproject.repository.VerificationRepository

class RepositoryManager private constructor(context: Context) {

    companion object {

        @Volatile
        private var INSTANCE: RepositoryManager? = null

        fun getInstance(context: Context): RepositoryManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RepositoryManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    // Lazy initialization of all repositories
    val auth: AuthRepository by lazy { AuthRepository(context) }
    val trainingCenter: TrainingCenterRepository by lazy { TrainingCenterRepository(context) }
    val fieldVerification: FieldVerificationRepository by lazy { FieldVerificationRepository(context) }
    val infrastructure: InfrastructureRepository by lazy { InfrastructureRepository(context) }
    val location: LocationRepository by lazy { LocationRepository(context) }
    val academic: AcademicRepository by lazy { AcademicRepository(context) }
    val residentialFacility: ResidentialFacilityRepository by lazy { ResidentialFacilityRepository(context) }
    val verification: VerificationRepository by lazy { VerificationRepository(context) }
    val rfOperations: RfOperationsRepository by lazy { RfOperationsRepository(context) }
}
