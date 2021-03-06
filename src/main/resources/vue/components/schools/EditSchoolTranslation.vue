<template>
  <div v-if="isOpened" class="mt-5 mb-5">
    <h5>Edit school translation</h5>
    <hr />
    <b-form-select
      v-model="selectedLocale"
      :options="locales"
      size="sm" class="mb-2"
      @change="getTranslation">
        <template slot="first">
          <option :value="null" disabled>-- Choose locale --</option>
        </template>
    </b-form-select>

    <b-form  @submit.stop.prevent>
        <label for="translation">Translation</label>
        <b-input v-model="form.translation.value" :state="form.translation.isValid" id="translation"></b-input>
        <b-form-invalid-feedback :state="form.translation.isValid">
            <div v-for="validationMsg in form.translation.validationMsgs" :key="validationMsg">{{validationMsg}}</div>
        </b-form-invalid-feedback>

        <b-button-group size="md" class="mt-3">
          <b-button variant="success" :disabled="addBtn.disabled" @click="addTranslation">Add</b-button>
          <b-button variant="primary" :disabled="editBtn.disabled" @click="editTranslation">Edit</b-button>
          <b-button variant="danger" :disabled="removeBtn.disabled" @click="removeTranslation">Remove</b-button>
        </b-button-group>
    </b-form>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import _ from 'lodash'
import axios from 'axios'
import EventBus from '@/EventBus'
import FormField from '@/form/FormField'
import Component from 'vue-class-component'
import { mapState } from 'vuex'
import SchoolTranslationService from '@/services/SchoolTranslationService'
import {showValidationErrors, clearValidationMsgs} from '@/util/FormUtil'

@Component({
  computed: {
    ...mapState({
      locales: function(state: any) {
        let localesForSelect = state.localesStore.localesForSelect;
        this.setDefaultLocale(localesForSelect);
        return localesForSelect;
      }
    })
  }
})
export default class EditSchoolTranslation extends Vue {
  private selectedLocale;
  private isOpened: boolean;
  private form;
  private selectedSchool;
  private userDefaultLocale;

  private addBtn = {disabled: false};
  private editBtn = {disabled: false};
  private removeBtn = {disabled: false};

  constructor() {
    super();
    this.selectedLocale = null;
    this.isOpened = false;
    this.form = {
      translation: new FormField()
    }
  }

  mounted() {
    let _this = this;
    EventBus.$on('show-edit-school-translation-form', function (item) {
      _this.isOpened = true;
      _this.form.translation.value = item.translation;
      _this.selectedSchool = item;
      
      let localesForSelect = _this.$store.state.localesStore.localesForSelect;
      _this.setDefaultLocale(localesForSelect);
      _this.selectedLocale = _this.userDefaultLocale;
      
      _this.getTranslation();
    });

    EventBus.$on('hide-edit-school-translation-form', function () {
      _this.isOpened = false;
    });

    EventBus.$on('school-translation-was-removed', function (schoolTranslation) {
      _this.addBtn.disabled = false;
      _this.editBtn.disabled = true;
      _this.removeBtn.disabled = true;
    });
  }

  setDefaultLocale(localesForSelect) {
    for (let locale of localesForSelect) {
      if (locale.active) {
        this.selectedLocale = locale.value;
        this.userDefaultLocale = locale.value;
      }
    }
  }
  
  getTranslation() {
    let schoolId = this.selectedSchool.id;
    let localeId = this.selectedLocale;
    SchoolTranslationService.getTranslation(schoolId, localeId)
      .then((response: any) => {
        this.form.translation.value = response.data.translation;
        this.addBtn.disabled = true;
        this.editBtn.disabled = false;
        this.removeBtn.disabled = false;
      }).catch((error: any) => {
        if (error.status == 400) {
          this.addBtn.disabled = false;
          this.editBtn.disabled = true;
          this.removeBtn.disabled = true;
        } else {
          this.addBtn.disabled = true;
          this.editBtn.disabled = true;
          this.removeBtn.disabled = true;
        }
      });
  }

  addTranslation() {
    let schoolTranslation = {
      schoolId: this.selectedSchool.id,
      localeId: this.selectedLocale,
      translation: this.form.translation.value
    }
    SchoolTranslationService.addTranslation(
      this.selectedSchool.id,
      this.selectedLocale,
      this.form.translation.value
    )
    .then(response => {
      EventBus.$emit('school-translation-was-changed', schoolTranslation);
      this.$bvToast.toast(`School translation was added successfully!`, {
        title: 'Notification',
        autoHideDelay: 5000,
        appendToast: false
      });
      clearValidationMsgs(this.form);

      this.addBtn.disabled = true;
      this.editBtn.disabled = false;
      this.removeBtn.disabled = false;
    }).catch(error => {
        if (error.status == 400) {
          let data = error.data;
          showValidationErrors(this.form, data.details);
        }
    })
  }

  editTranslation() {
    let schoolTranslation = {
      schoolId: this.selectedSchool.id,
      localeId: this.selectedLocale,
      translation: this.form.translation.value
    }
    SchoolTranslationService.editTranslation(
      this.selectedSchool.id,
      this.selectedLocale,
      this.form.translation.value
    )
    .then(response => {
      EventBus.$emit('school-translation-was-changed', schoolTranslation);
      this.$bvToast.toast(`School translation was edited successfully!`, {
        title: 'Notification',
        autoHideDelay: 5000,
        appendToast: false
      });
      clearValidationMsgs(this.form);
    }).catch(error => {
        if (error.status == 400) {
          let data = error.data;
          showValidationErrors(this.form, data.details);
        }
    })
  }

  removeTranslation() {
    EventBus.$emit('show-remove-school-translation-modal', this.selectedSchool, this.selectedLocale);
  }
}
</script>

