package org.geotools.data.mongodb;

import java.net.URI;
import java.util.Set;

import org.geotools.data.ResourceInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

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
public class MongoResourceInfo implements ResourceInfo {

    private MongoFeatureSource myFS;
    private URI myURI;

    public MongoResourceInfo(MongoFeatureSource fs) {
        this.myFS = fs;
        this.myURI = URI.create(this.myFS.getLayer().getSchema().getName().getNamespaceURI());
    }

    @Override
    public CoordinateReferenceSystem getCRS() {
        return this.myFS.getLayer().getCRS();
    }

    @Override
    public ReferencedEnvelope getBounds() {
        return this.myFS.getBounds();
    }

    @Override
    public URI getSchema() {
        return this.myURI;
    }

    @Override
    public String getName() {
        return this.myFS.getLayer().getName();
    }

    @Override
    public String getDescription() {
        return "MongoDB Resource";
    }

    @Override
    public Set<String> getKeywords() {
        return this.myFS.getLayer().getKeywords();
    }

    @Override
    public String getTitle() {
        if (this.myFS != null && this.myFS.getLayer().getSchema() != null
                && this.myFS.getLayer().getSchema().getDescription() != null) {
            return this.myFS.getLayer().getSchema().getDescription().toString();
        } else {
            return "null";
        }
    }
}
