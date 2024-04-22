package it.unibz.butterfly_net.core.ingestion.core;

public interface SpyRawDataRepository extends RawDataRepository {
    RawDataEntry getLastEntry();
}
