/*
 * Copyright 2020, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */

const SET_SERVER_ERROR = "SET_SERVER_ERROR";
const RESET_SERVER_ERROR = "RESET_SERVER_ERROR";
const SET_NETWORK_ERROR = "SET_NETWORK_ERROR";
const RESET_NETWORK_ERROR = "RESET_NETWORK_ERROR";
const SHOW_MODAL = "SHOW_MODAL";
const HIDE_MODAL = "HIDE_MODAL";

/**
 * address-space
 */
const DELETE_MESSAGING_PROJECT = "DELETE_MESSAGING_PROJECT";
const EDIT_ADDRESS_SPACE = "EDIT_ADDRESS_SPACE";
const CREATE_ADDRESS_SPACE = "CREATE_ADDRESS_SPACE";

/**
 * project
 */
const CREATE_PROJECT = "CREATE_PROJECT";
const DELETE_PROJECT = "DELETE_PROJECT";
const EDIT_PROJECT = "EDIT_PROJECT";

/**
 * address
 */
const DELETE_ADDRESS = "DELETE_ADDRESS";
const PURGE_ADDRESS = "PURGE_ADDRESS";
const CREATE_ADDRESS = "CREATE_ADDRESS";
const EDIT_ADDRESS = "EDIT_ADDRESS";
const LEAVE_CREATE_DEVICE = "LEAVE_CREATE_DEVICE";
const CLOSE_CONNECTIONS = "CLOSE_CONNECTIONS";

/**
 * Device and device details
 */
const UPDATE_PASSWORD = "UPDATE_PASSWORD";
const DELETE_IOT_DEVICE = "DELETE_IOT_DEVICE";
const SET_DEVICE_ACTION_TYPE = "SET_DEVICE_ACTION_TYPE";
const RESET_DEVICE_ACTION_TYPE = "RESET_DEVICE_ACTION_TYPE";
const UPDATE_DEVICE_STATUS = "UPDATE_DEVICE_STATUS";
const UPDATE_DEVICE_CREDENTIAL_STATUS = "UPDATE_DEVICE_CREDENTIAL_STATUS";
const CHANGE_CONNECTION_TYPE = "CHANGE_CONNECTION_TYPE";
const CLONE_DEVICE = "CLONE_DEVICE";
const REMOVE_CREDENTIALS = "REMOVE_CREDENTIALS";
const REMOVE_GATEWAY = "REMOVE_GATEWAY";

const types = {
  SET_SERVER_ERROR,
  RESET_SERVER_ERROR,
  SET_NETWORK_ERROR,
  RESET_NETWORK_ERROR,
  SHOW_MODAL,
  HIDE_MODAL,
  SET_DEVICE_ACTION_TYPE,
  RESET_DEVICE_ACTION_TYPE
};

const MODAL_TYPES = {
  DELETE_MESSAGING_PROJECT,
  EDIT_ADDRESS_SPACE,
  EDIT_ADDRESS,
  CREATE_ADDRESS_SPACE,
  CREATE_PROJECT,
  DELETE_ADDRESS,
  PURGE_ADDRESS,
  CREATE_ADDRESS,
  CLOSE_CONNECTIONS,
  LEAVE_CREATE_DEVICE,
  UPDATE_PASSWORD,
  DELETE_PROJECT,
  DELETE_IOT_DEVICE,
  UPDATE_DEVICE_STATUS,
  EDIT_PROJECT,
  UPDATE_DEVICE_CREDENTIAL_STATUS,
  CHANGE_CONNECTION_TYPE,
  CLONE_DEVICE,
  REMOVE_CREDENTIALS,
  REMOVE_GATEWAY
};

export { MODAL_TYPES, types };
