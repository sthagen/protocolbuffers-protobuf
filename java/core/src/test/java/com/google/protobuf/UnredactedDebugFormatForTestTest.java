package com.google.protobuf;

import static com.google.common.truth.Truth.assertThat;

import proto2_unittest.UnittestProto;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class UnredactedDebugFormatForTestTest {

  @Test
  public void unredactedDebugFormatForTestMessage_defaultMessageIsEmpty() {
    UnittestProto.TestEmptyMessage message = UnittestProto.TestEmptyMessage.getDefaultInstance();
    assertThat(UnredactedDebugFormatForTest.unredactedMultilineString(message)).isEmpty();

    UnittestProto.TestEmptyMessage singleLineMessage =
        UnittestProto.TestEmptyMessage.getDefaultInstance();
    assertThat(UnredactedDebugFormatForTest.unredactedSingleLineString(singleLineMessage))
        .isEmpty();
  }

  @Test
  public void unredactedDebugFormatForTestMessage_hasExpectedFormats() {
    UnittestProto.RedactedFields message =
        UnittestProto.RedactedFields.newBuilder()
            .setOptionalRedactedString("redacted")
            .setOptionalUnredactedString("hello")
            .setOptionalRedactedMessage(
                UnittestProto.TestNestedMessageRedaction.newBuilder()
                    .setOptionalRedactedNestedString("nested")
                    .build())
            .setOptionalUnredactedMessage(
                UnittestProto.TestNestedMessageRedaction.newBuilder()
                    .setOptionalUnredactedNestedString("unredacted")
                    .build())
            .build();
    assertThat(UnredactedDebugFormatForTest.unredactedMultilineString(message))
        .isEqualTo(
            "optional_redacted_string: \"redacted\"\n"
                + "optional_unredacted_string: \"hello\"\n"
                + "optional_redacted_message {\n"
                + "  optional_redacted_nested_string: \"nested\"\n"
                + "}\n"
                + "optional_unredacted_message {\n"
                + "  optional_unredacted_nested_string: \"unredacted\"\n"
                + "}\n");
    assertThat(UnredactedDebugFormatForTest.unredactedSingleLineString(message))
        .isEqualTo(
            "optional_redacted_string: \"redacted\""
                + " optional_unredacted_string: \"hello\""
                + " optional_redacted_message {"
                + " optional_redacted_nested_string: \"nested\""
                + " }"
                + " optional_unredacted_message {"
                + " optional_unredacted_nested_string: \"unredacted\""
                + " }");
  }

  private UnknownFieldSet makeUnknownFieldSet() {
    return UnknownFieldSet.newBuilder()
        .addField(
            5,
            UnknownFieldSet.Field.newBuilder()
                .addVarint(1)
                .addFixed32(2)
                .addFixed64(3)
                .addLengthDelimited(ByteString.copyFromUtf8("4"))
                .addLengthDelimited(
                    UnknownFieldSet.newBuilder()
                        .addField(12, UnknownFieldSet.Field.newBuilder().addVarint(6).build())
                        .build()
                        .toByteString())
                .addGroup(
                    UnknownFieldSet.newBuilder()
                        .addField(10, UnknownFieldSet.Field.newBuilder().addVarint(5).build())
                        .build())
                .build())
        .addField(
            8, UnknownFieldSet.Field.newBuilder().addVarint(1).addVarint(2).addVarint(3).build())
        .addField(
            15,
            UnknownFieldSet.Field.newBuilder()
                .addVarint(0xABCDEF1234567890L)
                .addFixed32(0xABCD1234)
                .addFixed64(0xABCDEF1234567890L)
                .build())
        .build();
  }

  @Test
  public void unredactedDebugFormatForTestUnknownFields_hasExpectedFormats() {
    UnknownFieldSet unknownFields = makeUnknownFieldSet();
    assertThat(UnredactedDebugFormatForTest.unredactedMultilineString(unknownFields))
        .isEqualTo(
            "5: 1\n"
                + "5: 0x00000002\n"
                + "5: 0x0000000000000003\n"
                + "5: \"4\"\n"
                + "5: {\n"
                + "  12: 6\n"
                + "}\n"
                + "5 {\n"
                + "  10: 5\n"
                + "}\n"
                + "8: 1\n"
                + "8: 2\n"
                + "8: 3\n"
                + "15: 12379813812177893520\n"
                + "15: 0xabcd1234\n"
                + "15: 0xabcdef1234567890\n");
    assertThat(UnredactedDebugFormatForTest.unredactedSingleLineString(unknownFields))
        .isEqualTo(
            "5: 1"
                + " 5: 0x00000002"
                + " 5: 0x0000000000000003"
                + " 5: \"4\""
                + " 5: {"
                + " 12: 6"
                + " }"
                + " 5 {"
                + " 10: 5"
                + " }"
                + " 8: 1"
                + " 8: 2"
                + " 8: 3"
                + " 15: 12379813812177893520"
                + " 15: 0xabcd1234"
                + " 15: 0xabcdef1234567890");
  }

  @Test
  public void unredactedToString_returnsTextFormat() {
    UnittestProto.RedactedFields message =
        UnittestProto.RedactedFields.newBuilder()
            .addRepeatedRedactedMessage(
                UnittestProto.TestNestedMessageRedaction.newBuilder()
                    .setOptionalRedactedNestedString("123")
                    .build())
            .addRepeatedRedactedMessage(
                UnittestProto.TestNestedMessageRedaction.newBuilder()
                    .setOptionalUnredactedNestedString("456")
                    .build())
            .build();
    assertThat(
            UnredactedDebugFormatForTest.unredactedToString(
                message.getRepeatedRedactedMessageList()))
        .isEqualTo(
            "[optional_redacted_nested_string: \"123\"\n"
                + ", optional_unredacted_nested_string: \"456\"\n"
                + "]");
  }

  @Test
  public void unredactedStringValueOf_returnsTextFormatForNonNullObject() {
    UnittestProto.RedactedFields message =
        UnittestProto.RedactedFields.newBuilder()
            .addRepeatedRedactedMessage(
                UnittestProto.TestNestedMessageRedaction.newBuilder()
                    .setOptionalRedactedNestedString("123")
                    .build())
            .addRepeatedRedactedMessage(
                UnittestProto.TestNestedMessageRedaction.newBuilder()
                    .setOptionalUnredactedNestedString("456")
                    .build())
            .build();
    assertThat(
            UnredactedDebugFormatForTest.unredactedStringValueOf(
                message.getRepeatedRedactedMessageList()))
        .isEqualTo(
            "[optional_redacted_nested_string: \"123\"\n"
                + ", optional_unredacted_nested_string: \"456\"\n"
                + "]");
  }

  @Test
  public void unredactedStringValueOf_returnsEmptyStringForNullObject() {
    UnittestProto.RedactedFields message = null;
    assertThat(UnredactedDebugFormatForTest.unredactedStringValueOf(message))
        .isEqualTo(String.valueOf(message));
  }

  @Test
  public void unredactedToStringArray_returnsTextFormat() {
    UnittestProto.RedactedFields message =
        UnittestProto.RedactedFields.newBuilder()
            .addRepeatedRedactedMessage(
                UnittestProto.TestNestedMessageRedaction.newBuilder()
                    .setOptionalRedactedNestedString("123")
                    .build())
            .addRepeatedRedactedMessage(
                UnittestProto.TestNestedMessageRedaction.newBuilder()
                    .setOptionalUnredactedNestedString("456")
                    .build())
            .build();
    assertThat(
            UnredactedDebugFormatForTest.unredactedToStringArray(new Message[] {message, message}))
        .isEqualTo(
            new String[] {
              "repeated_redacted_message {\n"
                  + "  optional_redacted_nested_string: \"123\"\n"
                  + "}\n"
                  + "repeated_redacted_message {\n"
                  + "  optional_unredacted_nested_string: \"456\"\n"
                  + "}\n",
              "repeated_redacted_message {\n"
                  + "  optional_redacted_nested_string: \"123\"\n"
                  + "}\n"
                  + "repeated_redacted_message {\n"
                  + "  optional_unredacted_nested_string: \"456\"\n"
                  + "}\n"
            });
  }

  @Test
  public void unredactedToStringList_returnsTextFormat() {
    UnittestProto.RedactedFields message =
        UnittestProto.RedactedFields.newBuilder()
            .addRepeatedRedactedMessage(
                UnittestProto.TestNestedMessageRedaction.newBuilder()
                    .setOptionalRedactedNestedString("123")
                    .build())
            .addRepeatedRedactedMessage(
                UnittestProto.TestNestedMessageRedaction.newBuilder()
                    .setOptionalUnredactedNestedString("456")
                    .build())
            .build();
    assertThat(UnredactedDebugFormatForTest.unredactedToStringList(Arrays.asList(message, message)))
        .containsExactly(
            new String[] {
              "repeated_redacted_message {\n"
                  + "  optional_redacted_nested_string: \"123\"\n"
                  + "}\n"
                  + "repeated_redacted_message {\n"
                  + "  optional_unredacted_nested_string: \"456\"\n"
                  + "}\n",
              "repeated_redacted_message {\n"
                  + "  optional_redacted_nested_string: \"123\"\n"
                  + "}\n"
                  + "repeated_redacted_message {\n"
                  + "  optional_unredacted_nested_string: \"456\"\n"
                  + "}\n"
            });
  }
}
