import com.ktools.datasource.impala.ImpalaFactory;
import com.ktools.warehouse.manager.datasource.KDataSourceFactory;

module com.ktools.k.connectors.impala {
    requires com.ktools.warehouse;

    opens images;

    provides KDataSourceFactory with ImpalaFactory;
}