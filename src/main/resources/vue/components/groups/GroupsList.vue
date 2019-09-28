<template>
  <div>
    <b-alert v-model="dismissibleAlert" class="mb-0 mt-3" variant="primary" dismissible>
      <ul class="mb-0">
        <li>To get groups you have to choose only major!</li>
        <li>You can get groups translations with specific locale by choosing locale.</li>
        <li>You can sort majors by school.</li>
      </ul>
    </b-alert>

    <b-form-select
      v-model="selectedSchool"
      :options="schools"
      size="sm"
      id="schoolSelect" class="mt-3"
      @change="getMajorsBySchoolId">
        <template slot="first">
          <option :value="null" disabled>-- Choose school --</option>
        </template>             
    </b-form-select>

    <b-form-select
      v-model="selectedMajor"
      :options="majors"
      size="sm"
      id="majorSelect" class="mt-3"
      @change="getGroupsByMajorId">
        <template slot="first">
          <option :value="null" disabled>-- Choose major --</option>
        </template>             
    </b-form-select>

    <b-form-select
      v-model="selectedLocale"
      :options="locales"
      size="sm" class="mt-3 mb-3"
      @change="getGroupsByMajorIdAndLocaleId">
        <template slot="first">
          <option :value="null" disabled>-- Choose locale --</option>
        </template>             
    </b-form-select>

    <b-table responsive striped hover small :items="groups" :fields="fields">
      <template slot="controls" slot-scope="row">
        <b-button squared variant="primary" @click="showEditMajorForm(row)">Edit</b-button>
        <b-button squared variant="danger" @click="showRemoveMajorModal(row)">Remove</b-button>
      </template>
    </b-table>
    <b-button block variant="success" @click="showAddMajorForm()" class="mb-5">Add</b-button>
  </div>
</template>


<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import { mapState } from 'vuex'
import MajorService from '@/services/MajorService'
import GroupService from '@/services/GroupService'
import EventBus from '@/EventBus'


@Component({
  computed: {
    ...mapState({
      schools: (state: any) => {
        return state.schoolsStore.schools.map(function (school) {
          return {value: school.id, text: school.name};
        });
      },
      majors: (state:any) => {
        return state.majorsStore.majors.map(function (major) {
          return {value: major.id, text: major.name};
        })
      },
      locales: function(state:any) {
        return state.localesStore.localesForSelect;
      },
      groups: function (state:any) {
        return state.groupsStore.groups;
      }
    })
  }
})
export default class GroupsList extends Vue {

  public dismissibleAlert = true;
  public selectedLocale: any;
  public selectedSchool: any;
  public selectedMajor: any;
  public fields: any;

  constructor() {
    super();
    this.selectedLocale = null;
    this.selectedSchool = null;
    this.selectedMajor = null;
    this.fields = {
      id: {
        label: '#',
        sortable: true
      },
      name: {
        label: 'Name',
        sortable: true
      },
      translation: {
        key: 'nameTranslation',
        label: 'Translation',
        sortable: true
      },
      course_num: {
        key: 'courseNum',
        label: 'Course',
        sortable: true
      },
      controls: {
        key: 'controls',
        label: 'Controls',
        sortable: false,
        tdClass: 'group-controls'
      },
    }
  }

  created() {
    this.$store.dispatch('getSchools');
    this.$store.dispatch('getMajors');
    let _this = this;

    EventBus.$on('group-was-updated', async function (groupId) {
      let updatedGroup;
      if (_this.selectedLocale == null) {
        updatedGroup = await GroupService.getByGroupId(groupId);
      } else {
        updatedGroup = await GroupService.getByGroupIdAndLocaleId(groupId, _this.selectedLocale);
      }
      _this.$store.commit('updateGroup', updatedGroup);
    });

    EventBus.$on('major-translation-was-changed', function (majorTranslation) {
      if (majorTranslation.localeId == _this.selectedLocale) {
        _this.$store.commit('updateMajorTranslation', majorTranslation);
      }
    });
  }

  async getMajorsBySchoolId() {
    let data = await MajorService.getAllBySchoolId(this.selectedSchool);
    this.$store.commit('setMajors', data);
  }

  async getGroupsByMajorId() {
    if (this.selectedLocale != null) {
      this.getGroupsByMajorIdAndLocaleId();
      return;
    }
    let groups = await GroupService.getGroupsByMajorId(this.selectedMajor);
    this.$store.commit('setGroups', groups);
  }

  async getGroupsByMajorIdAndLocaleId() {
    let groups = await GroupService.getGroupsByMajorIdAndLocaleId(this.selectedMajor, this.selectedLocale);
    this.$store.commit('setGroups', groups);
  }

  showEditMajorForm(row) {
    EventBus.$emit('hide-add-group-form');
    EventBus.$emit('show-edit-group-form', row.item);
    EventBus.$emit('show-edit-group-translation-form', row.item);
  }

  showRemoveMajorModal(row) {
    EventBus.$emit('show-remove-major-modal', row.item);
  }
}
</script>

<style lang="scss" scoped>
/deep/.group-controls {
  width: 149px;
  min-width: 149px;
}
</style>