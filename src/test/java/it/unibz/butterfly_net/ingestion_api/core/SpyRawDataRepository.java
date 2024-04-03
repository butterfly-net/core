package it.unibz.butterfly_net.ingestion_api.core;

public interface SpyRawDataRepository extends RawDataRepository {
    RawDataEntry getLastEntry();
}
