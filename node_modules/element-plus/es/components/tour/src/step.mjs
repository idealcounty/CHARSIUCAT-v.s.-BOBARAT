import '../../../utils/index.mjs';
import { tourContentProps } from './content.mjs';
import { buildProps, definePropType } from '../../../utils/vue/props/runtime.mjs';
import { iconPropType } from '../../../utils/vue/icon.mjs';

const tourStepProps = buildProps({
  target: {
    type: definePropType([String, Object, Function])
  },
  title: String,
  description: String,
  showClose: {
    type: Boolean,
    default: true
  },
  closeIcon: {
    type: iconPropType
  },
  showArrow: {
    type: Boolean,
    default: true
  },
  placement: tourContentProps.placement,
  mask: {
    type: definePropType([Boolean, Object])
  },
  contentStyle: {
    type: definePropType([Object])
  },
  prevButtonProps: {
    type: definePropType(Object)
  },
  nextButtonProps: {
    type: definePropType(Object)
  },
  scrollIntoViewOptions: {
    type: definePropType([Boolean, Object])
  },
  type: {
    type: definePropType(String)
  }
});
const tourStepEmits = {
  close: () => true
};

export { tourStepEmits, tourStepProps };
//# sourceMappingURL=step.mjs.map
