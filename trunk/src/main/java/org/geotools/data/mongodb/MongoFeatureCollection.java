package org.geotools.data.mongodb;

import org.geotools.data.FeatureReader;
import org.geotools.data.store.DataFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 * @author Gerald Gay, Data Tactics Corp.
 * @source $URL$
 * 
 *         (C) 2011, Open Source Geospatial Foundation (OSGeo)
 * 
 * @see The GNU Lesser General Public License (LGPL)
 */
/* This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA */
public class MongoFeatureCollection extends DataFeatureCollection {

    private MongoResultSet results = null;

    public MongoFeatureCollection(MongoResultSet rs) {
        this.results = rs;
    }

    @Override
    public int getCount() {
        return this.results.getCount();
    }

    @Override
    public ReferencedEnvelope getBounds() {
        return this.results.getBounds();
    }

    @Override
    public SimpleFeatureType getSchema() {
        return this.results.getSchema();
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() {
        return new MongoFeatureReader(this.results);
    }
}
