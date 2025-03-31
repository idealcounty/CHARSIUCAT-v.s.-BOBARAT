import { defineComponent } from 'vue';
import '../../../utils/index.mjs';
import { getNormalizedProps, isSameSteps } from './helper.mjs';
import { isArray } from '@vue/shared';
import { flattedChildren } from '../../../utils/vue/vnode.mjs';

var ElTourSteps = defineComponent({
  name: "ElTourSteps",
  props: {
    current: {
      type: Number,
      default: 0
    }
  },
  emits: ["update-steps"],
  setup(props, { slots, emit }) {
    let cachedSteps = [];
    return () => {
      var _a, _b;
      const children = (_a = slots.default) == null ? void 0 : _a.call(slots);
      const filteredSteps = [];
      const result = [];
      function filterSteps(children2) {
        if (!isArray(children2))
          return;
        children2.forEach((item) => {
          var _a2;
          const name = (_a2 = (item == null ? void 0 : item.type) || {}) == null ? void 0 : _a2.name;
          if (name === "ElTourStep") {
            const booleanKeys = ["showArrow", "mask", "scrollIntoViewOptions"];
            filteredSteps.push(getNormalizedProps(item, booleanKeys));
            result.push(item);
          }
        });
      }
      if (children.length) {
        filterSteps(flattedChildren((_b = children[0]) == null ? void 0 : _b.children));
      }
      if (!isSameSteps(filteredSteps, cachedSteps)) {
        cachedSteps = filteredSteps;
        emit("update-steps", filteredSteps);
      }
      if (result.length) {
        return result[props.current];
      }
      return null;
    };
  }
});

export { ElTourSteps as default };
//# sourceMappingURL=steps.mjs.map
