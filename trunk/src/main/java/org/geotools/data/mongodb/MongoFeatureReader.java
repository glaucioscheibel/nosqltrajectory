package org.geotools.data.mongodb;

import org.geotools.data.FeatureReader;
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
public class MongoFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private MongoResultSet results;
    private int next = 0;

    public MongoFeatureReader(MongoResultSet rs) {
        this.results = rs;
    }

    @Override
    public void close() {}

    @Override
    public boolean hasNext() {
        return (this.next < this.results.getCount());
    }

    @Override
    public SimpleFeature next() {
        return this.results.getFeature(this.next++);
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return this.results.getSchema();
    }

}
