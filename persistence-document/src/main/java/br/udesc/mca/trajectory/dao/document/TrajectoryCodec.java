package br.udesc.mca.trajectory.dao.document;

import java.util.Date;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectoryPoint;
import br.udesc.mca.trajectory.model.TrajectoryPointData;
import br.udesc.mca.trajectory.model.TrajectoryProcess;
import br.udesc.mca.trajectory.model.TrajectorySegment;
import br.udesc.mca.trajectory.model.TrajectorySegmentData;
import br.udesc.mca.trajectory.model.TrajectoryType;
import br.udesc.mca.trajectory.model.TrajectoryVersion;
import br.udesc.mca.trajectory.model.TransportationMode;
import br.udesc.mca.trajectory.model.User;

public class TrajectoryCodec implements Codec {
    @Override
    public Object decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Trajectory ret = new Trajectory();
        bsonReader.readStartDocument();
        BsonType type = bsonReader.readBsonType();
        while (!type.equals(BsonType.END_OF_DOCUMENT)) {
            switch (bsonReader.readName()) {
            case "_id":
                ret.setId(bsonReader.readInt64());
                break;
            case "description":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setDescription(bsonReader.readString());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "lastModified":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setLastModified(new Date(bsonReader.readInt64()));
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "originalTrajectory":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setOriginalTrajectory(bsonReader.readInt32());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "versions":
                bsonReader.readStartArray();
                type = bsonReader.readBsonType();
                while (type.equals(BsonType.DOCUMENT)) {
                    ret.addVersion(readVersion(bsonReader));
                    type = bsonReader.readBsonType();
                }
                bsonReader.readEndArray();
                break;
            }
            type = bsonReader.readBsonType();
        }
        bsonReader.readEndDocument();
        return ret;

    }

    @Override
    public void encode(BsonWriter w, Object o, EncoderContext encoderContext) {
        Trajectory t = (Trajectory) o;
        w.writeStartDocument();
        write(w, "_id", t.getId());
        write(w, "description", t.getDescription());
        write(w, "lastModified", t.getLastModified());
        write(w, "originalTrajectory", t.getOriginalTrajectory());
        w.writeStartArray("versions");
        if (t.getVersions() != null) {
            for (TrajectoryVersion v : t.getVersions()) {
                w.writeStartDocument();
                write(w, "id", v.getId());
                write(w, "version", v.getVersion());
                write(w, "lastModified", v.getLastModified());
                write(w, "previousVersion", v.getPreviousVersion());
                TrajectoryType type = v.getType();
                if (type != null) {
                    write(w, "type", v.getType().toString());
                } else {
                    write(w, "type", null);
                }
                User u = v.getUser();
                if (u != null) {
                    w.writeName("user");
                    w.writeStartDocument();
                    write(w, "id", u.getId());
                    write(w, "name", u.getName());
                    w.writeEndDocument();
                } else {
                    write(w, "user", null);
                }
                TrajectoryProcess p = v.getProcess();
                if (p != null) {
                    w.writeName("process");
                    w.writeStartDocument();
                    write(w, "componentId", p.getComponentId());
                    write(w, "executionDuration", p.getExecutionDuration());
                    write(w, "executionTime", p.getExecutionTime());
                    w.writeEndDocument();
                } else {
                    write(w, "process", null);
                }

                w.writeStartArray("segments");
                if (v.getSegments() != null) {
                    for (TrajectorySegment s : v.getSegments()) {
                        w.writeStartDocument();
                        TransportationMode mode = s.getTransportationMode();
                        w.writeName("transportationMode");
                        if (mode != null) {
                            w.writeString(mode.toString());
                        } else {
                            w.writeNull();
                        }
                        w.writeStartArray("data");
                        if (s.getData() != null) {
                            for (TrajectorySegmentData data : s.getData()) {
                                w.writeStartDocument();
                                write(w, "id", data.getId());
                                write(w, "key", data.getKey());
                                write(w, "value", data.getValue());
                                w.writeEndDocument();
                            }
                        }
                        w.writeEndArray();

                        w.writeStartArray("points");
                        if (s.getPoints() != null) {
                            for (TrajectoryPoint point : s.getPoints()) {
                                w.writeStartDocument();
                                write(w, "id", point.getId());
                                write(w, "h", point.getH());
                                write(w, "lat", point.getLat());
                                write(w, "lng", point.getLng());
                                write(w, "timestamp", point.getTimestamp());
                                w.writeStartArray("data");
                                if (point.getData() != null) {
                                    for (TrajectoryPointData data : point.getData()) {
                                        w.writeStartDocument();
                                        write(w, "id", data.getId());
                                        write(w, "key", data.getKey());
                                        write(w, "value", data.getValue());
                                        w.writeEndDocument();
                                    }
                                }
                                w.writeEndArray();
                                w.writeEndDocument();
                            }
                        }
                        w.writeEndArray();
                        w.writeEndDocument();
                    }
                }
                w.writeEndArray();
                w.writeEndDocument();
            }
        }
        w.writeEndArray();
        w.writeEndDocument();
    }

    private void write(BsonWriter w, String name, Object value) {
        w.writeName(name);
        if (value == null) {
            w.writeNull();
        } else {
            if (value instanceof Date) {
                w.writeInt64(((Date) value).getTime());
            } else if (value instanceof Long) {
                w.writeInt64(((Long) value));
            } else if (value instanceof Integer) {
                w.writeInt32((Integer) value);
            } else if (value instanceof String) {
                w.writeString((String) value);
            } else if (value instanceof Float) {
                w.writeDouble((Float) value);
            }
        }
    }

    private TrajectoryVersion readVersion(BsonReader bsonReader) {
        TrajectoryVersion ret = new TrajectoryVersion();
        BsonType type = BsonType.DOCUMENT;
        bsonReader.readStartDocument();
        while (!type.equals(BsonType.END_OF_DOCUMENT)) {
            switch (bsonReader.readName()) {
            case "id":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setId(bsonReader.readInt32());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "version":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setVersion(bsonReader.readInt32());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "lastModified":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setLastModified(new Date(bsonReader.readInt64()));
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "previousVersion":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setPreviousVersion(bsonReader.readInt32());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "type":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setType(TrajectoryType.valueOf(bsonReader.readString()));
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "user":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setUser(readUser(bsonReader));
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "process":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setHistory(readTrajectoryProcess(bsonReader));
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "segments":
                bsonReader.readStartArray();
                type = bsonReader.readBsonType();
                while (type.equals(BsonType.DOCUMENT)) {
                    ret.addSegment(readSegment(bsonReader));
                    type = bsonReader.readBsonType();
                }
                bsonReader.readEndArray();
                break;
            }
            type = bsonReader.readBsonType();
        }
        bsonReader.readEndDocument();
        return ret;
    }

    private TrajectorySegment readSegment(BsonReader bsonReader) {
        TrajectorySegment ret = new TrajectorySegment();
        BsonType type = BsonType.DOCUMENT;
        bsonReader.readStartDocument();
        while (!type.equals(BsonType.END_OF_DOCUMENT)) {
            switch (bsonReader.readName()) {
            case "transportationMode":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setTransportationMode(TransportationMode.valueOf(bsonReader.readString()));
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "data":
                bsonReader.readStartArray();
                type = bsonReader.readBsonType();
                while (type.equals(BsonType.DOCUMENT)) {
                    ret.addData(readTrajectorySegmentData(bsonReader));
                    type = bsonReader.readBsonType();
                }
                bsonReader.readEndArray();

                break;
            case "points":
                bsonReader.readStartArray();
                type = bsonReader.readBsonType();
                while (type.equals(BsonType.DOCUMENT)) {
                    ret.addPoint(readPoint(bsonReader));
                    type = bsonReader.readBsonType();
                }
                bsonReader.readEndArray();
                break;
            }
            type = bsonReader.readBsonType();
        }
        bsonReader.readEndDocument();
        return ret;
    }

    private TrajectoryPoint readPoint(BsonReader bsonReader) {
        TrajectoryPoint ret = new TrajectoryPoint();
        BsonType type = BsonType.DOCUMENT;
        bsonReader.readStartDocument();
        while (!type.equals(BsonType.END_OF_DOCUMENT)) {
            switch (bsonReader.readName()) {
            case "id":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setId(bsonReader.readInt32());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "h":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setH((float) bsonReader.readDouble());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "lat":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setLat((float) bsonReader.readDouble());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "lng":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setLng((float) bsonReader.readDouble());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "timestamp":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setTimestamp(bsonReader.readInt64());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "data":
                bsonReader.readStartArray();
                type = bsonReader.readBsonType();
                while (type.equals(BsonType.DOCUMENT)) {
                    ret.addData(readTrajectoryPointData(bsonReader));
                    type = bsonReader.readBsonType();
                }
                bsonReader.readEndArray();

                break;
            }
            type = bsonReader.readBsonType();
        }
        bsonReader.readEndDocument();
        return ret;
    }

    private User readUser(BsonReader bsonReader) {
        User ret = new User();
        BsonType type = BsonType.DOCUMENT;
        bsonReader.readStartDocument();
        while (!type.equals(BsonType.END_OF_DOCUMENT)) {
            switch (bsonReader.readName()) {
            case "id":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setId(bsonReader.readInt32());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "name":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setName(bsonReader.readString());
                } else {
                    bsonReader.skipValue();
                }
                break;
            }
            type = bsonReader.readBsonType();
        }
        bsonReader.readEndDocument();
        return ret;
    }

    private TrajectoryProcess readTrajectoryProcess(BsonReader bsonReader) {
        TrajectoryProcess ret = new TrajectoryProcess();
        BsonType type = BsonType.DOCUMENT;
        bsonReader.readStartDocument();
        while (!type.equals(BsonType.END_OF_DOCUMENT)) {
            switch (bsonReader.readName()) {
            case "componentId":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setComponentId(bsonReader.readInt32());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "executionDuration":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setExecutionDuration(bsonReader.readInt64());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "executionTime":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setExecutionTime(new Date(bsonReader.readInt64()));
                } else {
                    bsonReader.skipValue();
                }
                break;
            }
            type = bsonReader.readBsonType();
        }
        bsonReader.readEndDocument();
        return ret;
    }

    private TrajectoryPointData readTrajectoryPointData(BsonReader bsonReader) {
        TrajectoryPointData ret = new TrajectoryPointData();
        BsonType type = BsonType.DOCUMENT;
        bsonReader.readStartDocument();
        while (!type.equals(BsonType.END_OF_DOCUMENT)) {
            switch (bsonReader.readName()) {
            case "id":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setId(bsonReader.readInt32());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "key":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setKey(bsonReader.readString());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "value":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setValue(bsonReader.readString());
                } else {
                    bsonReader.skipValue();
                }
                break;
            }
            type = bsonReader.readBsonType();
        }
        bsonReader.readEndDocument();
        return ret;
    }

    private TrajectorySegmentData readTrajectorySegmentData(BsonReader bsonReader) {
        TrajectorySegmentData ret = new TrajectorySegmentData();
        BsonType type = BsonType.DOCUMENT;
        bsonReader.readStartDocument();
        while (!type.equals(BsonType.END_OF_DOCUMENT)) {
            switch (bsonReader.readName()) {
            case "id":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setId(bsonReader.readInt32());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "key":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setKey(bsonReader.readString());
                } else {
                    bsonReader.skipValue();
                }
                break;
            case "value":
                if (!bsonReader.getCurrentBsonType().equals(BsonType.NULL)) {
                    ret.setValue(bsonReader.readString());
                } else {
                    bsonReader.skipValue();
                }
                break;
            }
            type = bsonReader.readBsonType();
        }
        bsonReader.readEndDocument();
        return ret;
    }

    @Override
    public Class getEncoderClass() {
        return Trajectory.class;
    }
}
