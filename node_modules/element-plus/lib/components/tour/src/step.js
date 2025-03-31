'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

require('../../../utils/index.js');
var content = require('./content.js');
var runtime = require('../../../utils/vue/props/runtime.js');
var icon = require('../../../utils/vue/icon.js');

const tourStepProps = runtime.buildProps({
  target: {
    type: runtime.definePropType([String, Object, Function])
  },
  title: String,
  description: String,
  showClose: {
    type: Boolean,
    default: true
  },
  closeIcon: {
    type: icon.iconPropType
  },
  showArrow: {
    type: Boolean,
    default: true
  },
  placement: content.tourContentProps.placement,
  mask: {
    type: runtime.definePropType([Boolean, Object])
  },
  contentStyle: {
    type: runtime.definePropType([Object])
  },
  prevButtonProps: {
    type: runtime.definePropType(Object)
  },
  nextButtonProps: {
    type: runtime.definePropType(Object)
  },
  scrollIntoViewOptions: {
    type: runtime.definePropType([Boolean, Object])
  },
  type: {
    type: runtime.definePropType(String)
  }
});
const tourStepEmits = {
  close: () => true
};

exports.tourStepEmits = tourStepEmits;
exports.tourStepProps = tourStepProps;
//# sourceMappingURL=step.js.map
