/*
 Navicat Premium Data Transfer

 Source Server         : geodl_iecas
 Source Server Type    : PostgreSQL
 Source Server Version : 120003
 Source Host           : 192.168.9.64:32189
 Source Catalog        : geodl_iecas
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120003
 File Encoding         : 65001

 Date: 18/08/2022 16:08:48
*/

CREATE SEQUENCE file_satellite_sensor_relation_id_seq INCREMENT BY 1 START WITH 1 MAXVALUE 99_999_999;

-- ----------------------------
-- Table structure for file_satellite_sensor_relation
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_satellite_sensor_relation";
CREATE TABLE "public"."file_satellite_sensor_relation" (
  "id" int4 NOT NULL DEFAULT nextval('file_satellite_sensor_relation_id_seq'::regclass),
  "satellite_id" int4 NOT NULL,
  "sensor_id" int4 NOT NULL,
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."file_satellite_sensor_relation"."satellite_id" IS '卫星id';
COMMENT ON COLUMN "public"."file_satellite_sensor_relation"."sensor_id" IS '传感器id';

-- ----------------------------
-- Primary Key structure for table file_satellite_sensor_relation
-- ----------------------------
ALTER TABLE "public"."file_satellite_sensor_relation" ADD CONSTRAINT "file_satellite_sensor_relation_pkey" PRIMARY KEY ("id");
