{{- define "operations-hub.name" -}}
operations-hub
{{- end }}

{{- define "operations-hub.labels" -}}
app.kubernetes.io/part-of: {{ include "operations-hub.name" . }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
