import { init } from './appFetch';
import * as userService from './userService';
import * as templateService from './templateService';
import * as trackingService from './trackingService';
import * as commentService from './commentService';

export { default as NetworkError } from './NetworkError';
// eslint-disable-next-line
export default { init, userService, templateService, trackingService, commentService };
