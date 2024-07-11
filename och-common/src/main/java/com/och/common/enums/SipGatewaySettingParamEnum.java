package com.och.common.enums;

import lombok.Getter;

/**
 * 参考 https://freeswitch.org/confluence/display/FREESWITCH/Sofia+Configuration+Files
 * https://github.com/signalwire/freeswitch/blob/master/src/mod/endpoints/mod_sofia/conf/sofia.conf.xml
 *
 * @author danmo
 */
@Getter
public enum SipGatewaySettingParamEnum {
    /*
     * <settings>
     * When calls are in no media this will bring them back to media when
     * you press the hold button.
     * <param name="media-option" value="resume-media-on-hold"/>
     * This will allow a call after an attended transfer go back to bypass
     * media after an attended transfer.
     * <param name="media-option" value="bypass-media-after-att-xfer"/>
     * bypass again when hold is complete
     * <param name="media-option" value="bypass-media-after-hold"/>
     * <param name="user-agent-string" value="FreeSWITCH Rocks!"/>
     * <param name="debug" value="0"/>
     * If you want FreeSWITCH to shutdown if this profile fails to load,
     * uncomment the next line.
     * <param name="shutdown-on-fail" value="true"/>
     * <param name="sip-trace" value="no"/>
     * <param name="sip-capture" value="no"/>
     * <p>
     * Use presence_map.conf.xml to convert extension regex to presence
     * protos for routing
     * <param name="presence-proto-lookup" value="true"/>
     * <p>
     * <p>
     * Don't be picky about negotiated DTMF just always offer 2833 and
     * accept both 2833 and INFO
     * <param name="liberal-dtmf" value="true"/>
     * <p>
     * <p>
     * Sometimes, in extremely rare edge cases, the Sofia SIP stack may
     * stop responding. These options allow you to enable and control a
     * watchdog on the Sofia SIP stack so that if it stops responding for
     * the specified number of milliseconds, it will cause FreeSWITCH to
     * crash immediately. This is useful if you run in an HA environment
     * and need to ensure automated recovery from such a condition. Note
     * that if your server is idle a lot, the watchdog may fire due to not
     * receiving any SIP messages. Thus, if you expect your system to be
     * idle, you should leave the watchdog disabled. It can be toggled on
     * and off through the FreeSWITCH CLI either on an individual profile
     * basis or globally for all profiles. So, if you run in an HA
     * environment with a master and slave, you should use the CLI to make
     * sure the watchdog is only enabled on the master.
     * <p>
     * If such crash occurs, FreeSWITCH will dump core if allowed. The
     * stacktrace will include function watchdog_triggered_abort().
     *
     * <param name="watchdog-enabled" value="no"/>
     * <param name="watchdog-step-timeout" value="30000"/>
     * <param name="watchdog-event-timeout" value="30000"/>
     *
     * <param name="log-auth-failures" value="false"/>
     * <param name="forward-unsolicited-mwi-notify" value="false"/>
     *
     * <param name="context" value="public"/>
     * <param name="rfc2833-pt" value="101"/>
     * port to bind to for sip traffic
     * <param name="sip-port" value="$${internal_sip_port}"/>
     * <param name="dialplan" value="XML"/>
     * <param name="dtmf-duration" value="2000"/>
     * <param name="inbound-codec-prefs" value="$${global_codec_prefs}"/>
     * <param name="outbound-codec-prefs" value="$${global_codec_prefs}"/>
     * <param name="rtp-timer-name" value="soft"/>
     * ip address to use for rtp, DO NOT USE HOSTNAMES ONLY IP ADDRESSES
     * <param name="rtp-ip" value="$${local_ip_v4}"/>
     * ip address to bind to, DO NOT USE HOSTNAMES ONLY IP ADDRESSES
     * <param name="sip-ip" value="$${local_ip_v4}"/>
     * <param name="hold-music" value="$${hold_music}"/>
     * <param name="apply-nat-acl" value="nat.auto"/>
     * <p>
     * (default true) set to false if you do not wish to have called party
     * info in 1XX responses
     * <param name="cid-in-1xx" value="false"/>
     * <p>
     * extended info parsing
     * <param name="extended-info-parsing" value="true"/>
     *
     * <param name="aggressive-nat-detection" value="true"/>
     * There are known issues (asserts and segfaults) when 100rel is
     * enabled.  It is not recommended to enable 100rel at this time.
     * <param name="enable-100rel" value="true"/>
     * <p>
     * uncomment if you don't wish to try a next SRV destination on 503
     * response
     * RFC3263 Section 4.3
     * <param name="disable-srv503" value="true"/>
     * <p>
     * Enable Compact SIP headers.
     * <param name="enable-compact-headers" value="true"/>
     * enable/disable session timers
     * <param name="enable-timer" value="false"/>
     * <param name="minimum-session-expires" value="120"/>
     * <param name="apply-inbound-acl" value="domains"/>
     * This defines your local network, by default we detect your local
     * network and create this localnet.auto ACL for this.
     * <param name="local-network-acl" value="localnet.auto"/>
     * <param name="apply-register-acl" value="domains"/>
     * <param name="dtmf-type" value="info"/>
     * <p>
     * 'true' means every time 'first-only' means on the first register
     * <param name="send-message-query-on-register" value="true"/>
     * <p>
     * 'true' means every time 'first-only' means on the first register
     * <param name="send-presence-on-register" value="first-only"/>
     * <p>
     * Caller-ID type (choose one, can be overridden by inbound call type
     * and/or sip_cid_type channel variable
     * Remote-Party-ID header
     * <param name="caller-id-type" value="rpid"/>
     * <p>
     * P-*-Identity family of headers
     * <param name="caller-id-type" value="pid"/>
     * <p>
     * neither one
     * <param name="caller-id-type" value="none"/>
     *
     * <param name="record-path" value="$${recordings_dir}"/>
     * <param name="record-template" value="${caller_id_number}.${target_domain}.${strftime(%Y-%m-%d-%H-%M-%S)}.wav"/>
     * enable to use presence
     * <param name="manage-presence" value="true"/>
     * send a presence probe on each register to query devices to send
     * presence instead of sending presence with less info
     * <param name="presence-probe-on-register" value="true"/>
     * <param name="manage-shared-appearance" value="true"/>
     * used to share presence info across sofia profiles
     * Name of the db to use for this profile
     * <param name="dbname" value="share_presence"/>
     * <param name="presence-hosts" value="$${domain},$${local_ip_v4}"/>
     * <param name="presence-privacy" value="$${presence_privacy}"/>
     * <p>
     * This setting is for AAL2 bitpacking on G726
     * <param name="bitpacking" value="aal2"/>
     * max number of open dialogs in proceeding
     * <param name="max-proceeding" value="1000"/>
     * session timers for all call to expire after the specified seconds
     * <param name="session-timeout" value="1800"/>
     * Can be 'true' or 'contact'
     * <param name="multiple-registrations" value="contact"/>
     * set to 'greedy' if you want your codec list to take precedence
     * <param name="inbound-codec-negotiation" value="generous"/>
     * if you want to send any special bind params of your own
     * <param name="bind-params" value="transport=udp"/>
     * <param name="unregister-on-options-fail" value="true"/>
     * Send an OPTIONS packet to all registered endpoints
     * <param name="all-reg-options-ping" value="true"/>
     * Send an OPTIONS packet to NATed registered endpoints. Can be 'true' or 'udp-only
     * <param name="nat-options-ping" value="true"/>
     * <p>
     * TLS: disabled by default, set to "true" to enable
     * <param name="tls" value="$${internal_ssl_enable}"/>
     * Set to true to not bind on the normal sip-port but only on the TLS
     * port
     * <param name="tls-only" value="false"/>
     * additional bind parameters for TLS
     * <param name="tls-bind-params" value="transport=tls"/>
     * Port to listen on for TLS requests. (5061 will be used if
     * unspecified)
     * <param name="tls-sip-port" value="$${internal_tls_port}"/>
     * Location of the agent.pem and cafile.pem ssl certificates (needed
     * for TLS server)
     * <param name="tls-cert-dir" value="$${internal_ssl_dir}"/>
     * Optionally set the passphrase password used by openSSL to
     * encrypt/decrypt TLS private key files
     * <param name="tls-passphrase" value=""/>
     * Verify the date on TLS certificates
     * <param name="tls-verify-date" value="true"/>
     * TLS verify policy, when registering/inviting gateways with other
     * servers (outbound) or handling inbound registration/invite requests
     * how should we verify their certificate
     * set to 'in' to only verify incoming connections, 'out' to only
     * verify outgoing connections, 'all' to verify all connections, also
     * 'in_subjects', 'out_subjects' and 'all_subjects' for subject
     * validation. Multiple policies can be split with a '|' pipe
     * <param name="tls-verify-policy" value="none"/>
     * Certificate max verify depth to use for validating peer TLS
     * certificates when the verify policy is not none
     * <param name="tls-verify-depth" value="2"/>
     * If the tls-verify-policy is set to subjects_all or subjects_in this
     * sets which subjects are allowed, multiple subjects can be split
     * with a '|' pipe
     * <param name="tls-verify-in-subjects" value=""/>
     * Set the OpenSSL cipher suite list
     * <param name="tls-ciphers" value="!aNULL:!LOW:!EXP:!kECDH:!ECDSA:!DSS:!PSK:!SRP:ALL"/>
     * TLS version ("sslv23" (default), "tlsv1"). NOTE: Phones may not
     * work with TLSv1
     * <param name="tls-version" value="$${sip_tls_version}"/>
     * TLS maximum session lifetime
     * <param name="tls-timeout" value="300"/>
     * <p>
     * turn on auto-flush during bridge (skip timer sleep when the socket
     * already has data) (reduces delay on latent connections default
     * true, must be disabled explicitly)
     * <param name="rtp-autoflush-during-bridge" value="false"/>
     * <p>
     * If you don't want to pass through timestamps from 1 RTP call to
     * another (on a per call basis with rtp_rewrite_timestamps chanvar)
     *
     * <param name="rtp-rewrite-timestamps" value="true"/>
     * <param name="pass-rfc2833" value="true"/>
     * If you have ODBC support and a working dsn you can use it instead
     * of SQLite
     * <param name="odbc-dsn" value="dsn:user:pass"/>
     * <p>
     * Uncomment to set all inbound calls to no media mode
     * <param name="inbound-bypass-media" value="true"/>
     * <p>
     * Uncomment to set all inbound calls to proxy media mode
     * <param name="inbound-proxy-media" value="true"/>
     * <p>
     * Let calls hit the dialplan before selecting codec for the a-leg
     * <param name="inbound-late-negotiation" value="true"/>
     * <p>
     * Allow ZRTP clients to negotiate end-to-end security associations (also enables late negotiation)
     * <param name="inbound-zrtp-passthru" value="true"/>
     * <p>
     * this lets anything register
     * comment the next line and uncomment one or both of the other 2
     * lines for call authentication
     * <param name="accept-blind-reg" value="true"/>
     * <p>
     * accept any authentication without actually checking (not a good
     * feature for most people)
     * <param name="accept-blind-auth" value="true"/>
     * <p>
     * suppress CNG on this profile or per call with the 'suppress_cng'
     * variable
     * <param name="suppress-cng" value="true"/>
     * <p>
     * TTL for nonce in sip auth
     * <param name="nonce-ttl" value="60"/>
     * Uncomment if you want to force the outbound leg of a bridge to only
     * offer the codec that the originator is using
     * <param name="disable-transcoding" value="true"/>
     * Handle 302 Redirect in the dialplan
     * <param name="manual-redirect" value="true"/>
     * Disable Transfer
     * <param name="disable-transfer" value="true"/>
     * Disable Register
     * <param name="disable-register" value="true"/>
     * Used for when phones respond to a challenged ACK with method INVITE
     * in the hash
     * <param name="NDLB-broken-auth-hash" value="true"/>
     * add a ;received="<ip>:<port>" to the contact when replying to
     * register for nat handling
     * <param name="NDLB-received-in-nat-reg-contact" value="true"/>
     * <param name="auth-calls" value="$${internal_auth_calls}"/>
     * <param name="auth-messages" value="false"/>
     * Force the user and auth-user to match.
     * <param name="inbound-reg-force-matching-username" value="true"/>
     * on authed calls, authenticate *all* the packets not just invite
     * <param name="auth-all-packets" value="false"/>
     * <p>
     * external_sip_ip
     * Used as the public IP address for SDP.
     * Can be an one of:
     * ip address            - "12.34.56.78"
     * a stun server lookup  - "stun:stun.server.com"
     * a DNS name            - "host:host.server.com"
     * auto                  - Use guessed ip.
     * auto-nat              - Use ip learned from NAT-PMP or UPNP
     *
     * <param name="ext-rtp-ip" value="auto-nat"/>
     * <param name="ext-sip-ip" value="auto-nat"/>
     * <p>
     * rtp inactivity timeout
     * <param name="rtp-timeout-sec" value="300"/>
     * <param name="rtp-hold-timeout-sec" value="1800"/>
     * <p> VAD choose one (out is a good choice); </p>
     * <param name="vad" value="in"/>
     * <param name="vad" value="out"/>
     * <param name="vad" value="both"/>
     * <param name="alias" value="sip:10.0.1.251:5555"/>
     * <p>
     * These are enabled to make the default config work better out of the
     * box.  If you need more than ONE domain you'll need to not use these
     * options.
     * <p>
     * all inbound reg will look in this domain for the users
     * <param name="force-register-domain" value="$${domain}"/>
     * force the domain in subscriptions to this value
     * <param name="force-subscription-domain" value="$${domain}"/>
     * all inbound reg will stored in the db using this domain
     * <param name="force-register-db-domain" value="$${domain}"/>
     *
     * <param name="delete-subs-on-register" value="false"/>
     * <p>
     * launch a new thread to process each new inbound register when using heavier backends
     * <param name="inbound-reg-in-new-thread" value="true"/>
     * <p>
     * enable rtcp on every channel also can be done per leg basis with
     * rtcp_audio_interval_msec variable set to passthru to pass it across
     * a call
     * <param name="rtcp-audio-interval-msec" value="5000"/>
     * <param name="rtcp-video-interval-msec" value="5000"/>
     * <p>
     * force suscription expires to a lower value than requested
     * <param name="force-subscription-expires" value="60"/>
     * <p>
     * add a random deviation to the expires value of the 202 Accepted
     * <param name="sip-subscription-max-deviation" value="120"/>
     * <p>
     * disable register and transfer which may be undesirable in a public
     * switch
     * <param name="disable-transfer" value="true"/>
     * <param name="disable-register" value="true"/>
     * <p>
     * <p>
     * enable-3pcc can be set to either 'true' or 'proxy', true accepts
     * the call right away, proxy waits until the call has been answered
     * then sends accepts
     *
     * <param name="enable-3pcc" value="true"/>
     * <p>
     * use at your own risk or if you know what this does.
     * <param name="NDLB-force-rport" value="true"/>
     * <p>
     * Choose the realm challenge key. Default is auto_to if not set.
     * <p>
     * auto_from  - uses the from field as the value for the sip realm.
     * auto_to    - uses the to field as the value for the sip realm.
     * <anyvalue> - you can input any value to use for the sip realm.
     * <p>
     * If you want URL dialing to work you'll want to set this to auto_from.
     * <p>
     * If you use any other value besides auto_to or auto_from you'll loose
     * the ability to do multiple domains.
     * <p>
     * Note: comment out to restore the behavior before 2008-09-29
     *
     *
     * <param name="challenge-realm" value="auto_from"/>
     * <param name="disable-rtp-auto-adjust" value="true"/>
     * on inbound calls make the uuid of the session equal to the sip call
     * id of that call
     * <param name="inbound-use-callid-as-uuid" value="true"/>
     * on outbound calls set the callid to match the uuid of the session
     *
     * <param name="outbound-use-uuid-as-callid" value="true"/>
     * set to false disable this feature
     * <param name="rtp-autofix-timing" value="false"/>
     * <p>
     * set this param to false if your gateway for some reason hates X-
     * headers that it is supposed to ignore
     * <param name="pass-callee-id" value="false"/>
     * <p>
     * clear clears them all or supply the name to add or the name
     * prefixed with ~ to remove valid values:
     * <p>
     * clear
     * CISCO_SKIP_MARK_BIT_2833
     * SONUS_SEND_INVALID_TIMESTAMP_2833
     *
     *
     * <param name="auto-rtp-bugs" data="clear"/>
     * <p>
     * the following can be used as workaround with bogus SRV/NAPTR
     * records
     * <param name="disable-srv" value="false" />
     * <param name="disable-naptr" value="false" />
     * <p>
     * The following can be used to fine-tune timers within sofia's
     * transport layer Those settings are for advanced users and can
     * safely be left as-is
     * <p>
     * Initial retransmission interval (in milliseconds).
     * <p>
     * Set the T1 retransmission interval used by the SIP transaction
     * engine.
     * <p>
     * The T1 is the initial duration used by request retransmission
     * timers A and E (UDP) as well as response retransmission timer G.
     *
     * <param name="timer-T1" value="500" />
     * <p>
     * Transaction timeout (defaults to T1 * 64).
     * <p>
     * Set the T1x64 timeout value used by the SIP transaction engine.
     * <p>
     * The T1x64 is duration used for timers B, F, H, and J (UDP) by the
     * SIP transaction engine.
     * <p>
     * The timeout value T1x64 can be adjusted separately from the initial
     * retransmission interval T1.
     * <param name="timer-T1X64" value="32000" />
     * <p>
     * <p>
     * Maximum retransmission interval (in milliseconds).
     * <p>
     * Set the maximum retransmission interval used by the SIP transaction
     * engine.
     * <p>
     * The T2 is the maximum duration used for the timers E (UDP) and G by
     * the SIP transaction engine.
     * <p>
     * Note that the timer A is not capped by T2. Retransmission interval
     * of INVITE requests grows exponentially until the timer B fires.
     *
     * <param name="timer-T2" value="4000" />
     * <p>
     * <p>
     * Transaction lifetime (in milliseconds).
     * <p>
     * Set the lifetime for completed transactions used by the SIP
     * transaction engine.
     * <p>
     * A completed transaction is kept around for the duration of T4 in
     * order to catch late responses.
     * <p>
     * The T4 is the maximum duration for the messages to stay in the
     * network and the duration of SIP timer K.
     * <param name="timer-T4" value="4000" />
     * <p>
     * Turn on a jitterbuffer for every call
     * <param name="auto-jitterbuffer-msec" value="60"/>
     * <p>
     * <p>
     * By default mod_sofia will ignore the codecs in the sdp for
     * hold/unhold operations Set this to true if you want to actually
     * parse the sdp and re-negotiate the codec during hold/unhold.  It's
     * probably not what you want so stick with the default unless you
     * really need to change this.
     *
     * <param name="renegotiate-codec-on-hold" value="true"/>
     * </settings>
     */
    MEDIA_OPTION("media-option", ""),
    USER_AGENT_STRING("user-agent-string", ""),
    DEBUG("debug", ""),
    SHUTDOWN_ON_FAIL("shutdown-on-fail", ""),
    SIP_TRACE("sip-trace", ""),
    SIP_CAPTURE("sip-capture", ""),
    PRESENCE_PROTO_LOOKUP("presence-proto-lookup", ""),
    LIBERAL_DTMF("liberal-dtmf", ""),
    WATCHDOG_ENABLED("watchdog-enabled", ""),
    WATCHDOG_STEP_TIMEOUT("watchdog-step-timeout", ""),
    WATCHDOG_EVENT_TIMEOUT("watchdog-event-timeout", ""),
    LOG_AUTH_FAILURES("log-auth-failures", ""),
    FORWARD_UNSOLICITED_MWI_NOTIFY("forward-unsolicited-mwi-notify", ""),
    CONTEXT("context", ""),
    RFC2833_PT("rfc2833-pt", ""),
    SIP_PORT("sip-port", ""),
    DIALPLAN("dialplan", ""),
    DTMF_DURATION("dtmf-duration", ""),
    INBOUND_CODEC_PREFS("inbound-codec-prefs", ""),
    OUTBOUND_CODEC_PREFS("outbound-codec-prefs", ""),
    RTP_TIMER_NAME("rtp-timer-name", ""),
    RTP_IP("rtp-ip", ""),
    SIP_IP("sip-ip", ""),
    HOLD_MUSIC("hold-music", ""),
    APPLY_NAT_ACL("apply-nat-acl", ""),
    CID_IN_1XX("cid-in-1xx", ""),
    EXTENDED_INFO_PARSING("extended-info-parsing", ""),
    AGGRESSIVE_NAT_DETECTION("aggressive-nat-detection", ""),
    ENABLE_100REL("enable-100rel", ""),
    DISABLE_SRV503("disable-srv503", ""),
    ENABLE_COMPACT_HEADERS("enable-compact-headers", ""),
    ENABLE_TIMER("enable-timer", ""),
    MINIMUM_SESSION_EXPIRES("minimum-session-expires", ""),
    APPLY_INBOUND_ACL("apply-inbound-acl", ""),
    LOCAL_NETWORK_ACL("local-network-acl", ""),
    APPLY_REGISTER_ACL("apply-register-acl", ""),
    APPLY_CANDIDATE_ACL("apply-candidate-acl", ""),
    DTMF_TYPE("dtmf-type", ""),
    SEND_MESSAGE_QUERY_ON_REGISTER("send-message-query-on-register", ""),
    SEND_PRESENCE_ON_REGISTER("send-presence-on-register", ""),
    CALLER_ID_TYPE("caller-id-type", ""),
    RECORD_PATH("record-path", ""),
    RECORD_TEMPLATE("record-template", ""),
    MANAGE_PRESENCE("manage-presence", ""),
    PRESENCE_PROBE_ON_REGISTER("presence-probe-on-register", ""),
    MANAGE_SHARED_APPEARANCE("manage-shared-appearance", ""),
    DBNAME("dbname", ""),
    PRESENCE_HOSTS("presence-hosts", ""),
    PRESENCE_PRIVACY("presence-privacy", ""),
    BITPACKING("bitpacking", ""),
    MAX_PROCEEDING("max-proceeding", ""),
    MAX_RECV_REQUESTS_PER_SECOND("max-recv-requests-per-second", ""),
    SESSION_TIMEOUT("session-timeout", ""),
    MULTIPLE_REGISTRATIONS("multiple-registrations", ""),
    INBOUND_CODEC_NEGOTIATION("inbound-codec-negotiation", ""),
    BIND_PARAMS("bind-params", ""),
    UNREGISTER_ON_OPTIONS_FAIL("unregister-on-options-fail", ""),
    ALL_REG_OPTIONS_PING("all-reg-options-ping", ""),
    NAT_OPTIONS_PING("nat-options-ping", ""),
    TLS("tls", ""),
    TLS_ONLY("tls-only", ""),
    TLS_BIND_PARAMS("tls-bind-params", ""),
    TLS_SIP_PORT("tls-sip-port", ""),
    TLS_CERT_DIR("tls-cert-dir", ""),
    TLS_PASSPHRASE("tls-passphrase", ""),
    TLS_VERIFY_DATE("tls-verify-date", ""),
    TLS_VERIFY_POLICY("tls-verify-policy", ""),
    TLS_VERIFY_DEPTH("tls-verify-depth", ""),
    TLS_VERIFY_IN_SUBJECTS("tls-verify-in-subjects", ""),
    TLS_CIPHERS("tls-ciphers", ""),
    TLS_VERSION("tls-version", ""),
    TLS_TIMEOUT("tls-timeout", ""),
    RTP_AUTOFLUSH_DURING_BRIDGE("rtp-autoflush-during-bridge", ""),
    RTP_REWRITE_TIMESTAMPS("rtp-rewrite-timestamps", ""),
    PASS_RFC2833("pass-rfc2833", ""),
    ODBC_DSN("odbc-dsn", ""),
    INBOUND_BYPASS_MEDIA("inbound-bypass-media", ""),
    INBOUND_PROXY_MEDIA("inbound-proxy-media", ""),
    INBOUND_LATE_NEGOTIATION("inbound-late-negotiation", ""),
    INBOUND_ZRTP_PASSTHRU("inbound-zrtp-passthru", ""),
    ACCEPT_BLIND_REG("accept-blind-reg", ""),
    ACCEPT_BLIND_AUTH("accept-blind-auth", ""),
    SUPPRESS_CNG("suppress-cng", ""),
    NONCE_TTL("nonce-ttl", ""),
    DISABLE_TRANSCODING("disable-transcoding", ""),
    MANUAL_REDIRECT("manual-redirect", ""),
    DISABLE_TRANSFER("disable-transfer", ""),
    DISABLE_REGISTER("disable-register", ""),
    NDLB_BROKEN_AUTH_HASH("NDLB-broken-auth-hash", ""),
    NDLB_RECEIVED_IN_NAT_REG_CONTACT("NDLB-received-in-nat-reg-contact", ""),
    AUTH_CALLS("auth-calls", ""),
    AUTH_SUBSCRIPTIONS("auth-subscriptions", ""),
    INBOUND_REG_FORCE_MATCHING_USERNAME("inbound-reg-force-matching-username", ""),
    AUTH_ALL_PACKETS("auth-all-packets", ""),
    AUTH_MESSAGES("auth-messages", ""),
    DISABLE_AUTH_MESSAGES("disable-auth-messages", ""),
    DISABLE_AUTH_SUBSCRIPTIONS("disable-auth-subscriptions", ""),
    EXT_RTP_IP("ext-rtp-ip", ""),
    EXT_SIP_IP("ext-sip-ip", ""),
    RTP_TIMEOUT_SEC("rtp-timeout-sec", ""),
    RTP_HOLD_TIMEOUT_SEC("rtp-hold-timeout-sec", ""),
    VAD("vad", ""),
    ALIAS("alias", ""),
    FORCE_REGISTER_DOMAIN("force-register-domain", ""),
    FORCE_SUBSCRIPTION_DOMAIN("force-subscription-domain", ""),
    FORCE_REGISTER_DB_DOMAIN("force-register-db-domain", ""),
    DELETE_SUBS_ON_REGISTER("delete-subs-on-register", ""),
    INBOUND_REG_IN_NEW_THREAD("inbound-reg-in-new-thread", ""),
    RTCP_AUDIO_INTERVAL_MSEC("rtcp-audio-interval-msec", ""),
    RTCP_VIDEO_INTERVAL_MSEC("rtcp-video-interval-msec", ""),
    FORCE_SUBSCRIPTION_EXPIRES("force-subscription-expires", ""),
    SIP_SUBSCRIPTION_MAX_DEVIATION("sip-subscription-max-deviation", ""),
    ENABLE_3PCC("enable-3pcc", ""),
    NDLB_FORCE_RPORT("NDLB-force-rport", ""),
    CHALLENGE_REALM("challenge-realm", ""),
    DISABLE_RTP_AUTO_ADJUST("disable-rtp-auto-adjust", ""),
    INBOUND_USE_CALLID_AS_UUID("inbound-use-callid-as-uuid", ""),
    OUTBOUND_USE_UUID_AS_CALLID("outbound-use-uuid-as-callid", ""),
    RTP_AUTOFIX_TIMING("rtp-autofix-timing", ""),
    PASS_CALLEE_ID("pass-callee-id", ""),
    AUTO_RTP_BUGS("auto-rtp-bugs", ""),
    DISABLE_SRV("disable-srv", ""),
    DISABLE_NAPTR("disable-naptr", ""),
    TIMER_T1("timer-T1", ""),
    TIMER_T1X64("timer-T1X64", ""),
    TIMER_T2("timer-T2", ""),
    TIMER_T4("timer-T4", ""),
    AUTO_JITTERBUFFER_MSEC("auto-jitterbuffer-msec", ""),
    RENEGOTIATE_CODEC_ON_HOLD("renegotiate-codec-on-hold", ""),
    PROXY_HOLD("proxy-hold", ""),
    PROXY_NOTIFY_EVENTS("proxy-notify-events", ""),
    WS_BINDING("ws-binding", ""),
    WSS_BINDING("wss-binding", ""),


    ;
    /**
     * 配置项值
     */
    public final String key;

    /**
     * 配置项说明
     */
    public final String msg;

    SipGatewaySettingParamEnum(String key, String msg) {
        this.key = key;
        this.msg = msg;
    }

}
