import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';

class OverviewNewtag extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
<vaadin-vertical-layout style="width: 100%; height: 100%;">
 <div id="div"></div>
</vaadin-vertical-layout>
`;
    }

    static get is() {
        return 'overview-newtag';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(OverviewNewtag.is, OverviewNewtag);
