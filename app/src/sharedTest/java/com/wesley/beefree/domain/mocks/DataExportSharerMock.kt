package com.wesley.beefree.domain.mocks

import com.wesley.beefree.domain.repository.ports.DataExportSharer

class DataExportSharerMock(
    var throwOnShare: Throwable? = null,
) : DataExportSharer {
    var shareCalls: Int = 0
        private set

    override suspend fun shareExportedData() {
        shareCalls++
        throwOnShare?.let { throw it }
    }
}
